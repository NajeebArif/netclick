package narif.poc.netclick.service;

import narif.poc.netclick.client.DeliveryClient;
import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.model.records.*;
import narif.poc.netclick.repository.reactive.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple3;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReactiveRentService {

    private static final Logger log = LoggerFactory.getLogger(ReactiveRentService.class);

    private final ReactiveFilmRepository reactiveFilmRepository;
    private final ReactiveCustomerRepository reactiveCustomerRepository;
    private final ReactiveStaffRepository reactiveStaffRepository;
    private final ReactiveInventoryRepository reactiveInventoryRepository;
    private final ReactiveRentalRepository reactiveRentalRepository;
    private final ReactivePaymentRepository reactivePaymentRepository;
    private final DeliveryClient deliveryClient;
    private List<Integer> rentalIds = new ArrayList<>();

    public ReactiveRentService(ReactiveFilmRepository reactiveFilmRepository, ReactiveCustomerRepository reactiveCustomerRepository, ReactiveStaffRepository reactiveStaffRepository, ReactiveInventoryRepository reactiveInventoryRepository, ReactiveRentalRepository reactiveRentalRepository, ReactivePaymentRepository reactivePaymentRepository, DeliveryClient deliveryClient) {
        this.reactiveFilmRepository = reactiveFilmRepository;
        this.reactiveCustomerRepository = reactiveCustomerRepository;
        this.reactiveStaffRepository = reactiveStaffRepository;
        this.reactiveInventoryRepository = reactiveInventoryRepository;
        this.reactiveRentalRepository = reactiveRentalRepository;
        this.reactivePaymentRepository = reactivePaymentRepository;
        this.deliveryClient = deliveryClient;
    }

    public Flux<Integer> rentFilm(RentMovieDto rentMovieDto){
        final var customerRecordMono = getCustomerRecordMono(rentMovieDto);
        final var staffRecordFlux = getStaffRecordMono();
        final var filmRecordFlux = getFilmRecordFlux(rentMovieDto);
        final var costMono = getCostMono(rentMovieDto, filmRecordFlux);
        final var rentalRecordFlux = createRentalRecordFlux(filmRecordFlux, customerRecordMono, staffRecordFlux);
        final var savedRentalRecordIdFlux = saveRentalRecordsAndGetIds(rentalRecordFlux)
                .doOnNext(rentalIds::add)
                .doOnComplete(this::deliverRentals);
        final var paymentRecordFlux = createPaymentRecordFlux(costMono, customerRecordMono, staffRecordFlux, savedRentalRecordIdFlux);
        final var savedPaymentRecordFlux = savePaymentRecords(paymentRecordFlux);
        return savedPaymentRecordFlux.map(PaymentRecord::rentalId);
    }

    private void deliverRentals(){
        deliveryClient.deliverRentedFilmsReactive(Flux.fromIterable(rentalIds))
                .subscribe(s -> {
                    log.info("Delivered rental ids: "+rentalIds+"? "+s);
                    rentalIds = new ArrayList<>();
                });
    }

    private static Mono<Double> getCostMono(RentMovieDto rentMovieDto, Flux<FilmRecord> filmRecordFlux) {
        return Mono.zip(filmRecordFlux.collectList(), Mono.just(rentMovieDto))
                .flatMap(t -> hypotheticalCostCalculation(t.getT2(), t.getT1()));
    }

    private static Flux<PaymentRecord> createPaymentRecordFlux(Mono<Double> costMono, Mono<CustomerRecord> customerRecordMono, Mono<StaffRecord> staffRecordFlux, Flux<Integer> savedRentalRecordIdFlux) {
        return savedRentalRecordIdFlux.flatMap(rentalId ->
                Mono.zip(costMono, customerRecordMono, staffRecordFlux)
                        .map(tuple3 -> createPaymentRecord(rentalId, tuple3)));
    }

    private Flux<RentalRecord> createRentalRecordFlux(Flux<FilmRecord> filmRecordFlux, Mono<CustomerRecord> customerRecordMono, Mono<StaffRecord> staffRecordFlux) {
        return filmRecordFlux.flatMap(filmRecord -> {
            final var inventoryRecordFlux = getInventoryRecordFlux(filmRecord);
            return Mono.zip(customerRecordMono, staffRecordFlux, Mono.from(inventoryRecordFlux))
                    .map(tuple -> createRentalRecord(tuple));
        });
    }

    private Flux<PaymentRecord> savePaymentRecords(Flux<PaymentRecord> paymentRecordFlux) {
        return reactivePaymentRepository
                .saveAll(paymentRecordFlux)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<Integer> saveRentalRecordsAndGetIds(Flux<RentalRecord> rentalRecordFlux) {
        return reactiveRentalRepository
                .saveAll(rentalRecordFlux)
                .map(RentalRecord::rentalId)
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Flux<FilmRecord> getFilmRecordFlux(RentMovieDto rentMovieDto) {
        return reactiveFilmRepository
                .findAllById(rentMovieDto.getFilmIds())
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<StaffRecord> getStaffRecordMono() {
        return reactiveStaffRepository
                .findById(Mono.just(1))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<CustomerRecord> getCustomerRecordMono(RentMovieDto rentMovieDto) {
        return reactiveCustomerRepository
                .findByEmail(rentMovieDto.getCustomerEmail())
                .subscribeOn(Schedulers.boundedElastic());
    }

    private static RentalRecord createRentalRecord(Tuple3<CustomerRecord, StaffRecord, InventoryRecord> tuple) {
        return RentalRecord.of(tuple.getT3().inventoryId(),
                tuple.getT1().customerId(), tuple.getT2().staffId());
    }

    private Flux<InventoryRecord> getInventoryRecordFlux(FilmRecord filmRecord) {
        return reactiveInventoryRepository
                .findAllByFilmId(filmRecord.filmId())
                .take(1);
    }

    private static PaymentRecord createPaymentRecord(Integer rentalId, Tuple3<Double, CustomerRecord, StaffRecord> tuple3) {
        return PaymentRecord.of(tuple3.getT2().customerId(), tuple3.getT3().staffId(),
                rentalId, tuple3.getT1());
    }

    private static Mono<Double> hypotheticalCostCalculation(RentMovieDto rentMovieDto, List<FilmRecord> filmList) {
        return Mono.justOrEmpty(filmList.stream().map(film -> film.rentalRate() * rentMovieDto.getNumberOfDays())
                .reduce(Double::sum));
    }

}
