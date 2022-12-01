package narif.poc.netclick.service;

import narif.poc.netclick.client.DeliveryClient;
import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.model.entity.*;
import narif.poc.netclick.repository.CustomerRepository;
import narif.poc.netclick.repository.PaymentRepository;
import narif.poc.netclick.repository.RentalRepository;
import narif.poc.netclick.repository.StaffRepository;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RentService {

    private final Logger log = LoggerFactory.getLogger(RentService.class);

    private final FilmService filmService;
    private final CustomerRepository customerRepository;
    private final RentalRepository rentalRepository;
    private final StaffRepository staffRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryClient deliveryClient;

    public RentService(FilmService filmService, CustomerRepository customerRepository, RentalRepository rentalRepository, StaffRepository staffRepository, PaymentRepository paymentRepository, DeliveryClient deliveryClient) {
        this.filmService = filmService;
        this.customerRepository = customerRepository;
        this.rentalRepository = rentalRepository;
        this.staffRepository = staffRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryClient = deliveryClient;
    }

    public Flux<Integer> rentFilm(RentMovieDto rentMovieDto) {
        final var filmFlux = Mono.fromCallable(()->filmService.findAllByIds(rentMovieDto.getFilmIds()))
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic());
        final var totalCostMono = calculateTotalCostOfRent(rentMovieDto, filmFlux);
        final var customerMono = Mono.fromCallable(()-> getCustomer(rentMovieDto));
        final var staffMono = Mono.fromCallable(this::getStaff);
        final var paymentMono = getPaymentMono(totalCostMono, customerMono, staffMono)
                .subscribeOn(Schedulers.boundedElastic());
        final var rentalFlux = filmFlux
                .flatMap(filmToRentals(rentMovieDto, customerMono, staffMono, paymentMono))
                .subscribeOn(Schedulers.boundedElastic());
        return Flux.zip(rentalFlux.collectList(), paymentMono)
                .flatMapIterable(tuple-> processRentals(tuple.getT1(), tuple.getT2()));
    }

    private List<Integer> processRentals(List<Rental> rentals, Payment payment){
        for (Rental ren : rentals) {
            payment.setRental(ren);
        }
        final List<Integer> rentalIds = saveRentalsToDb(rentals);
        savePaymentToDb(payment);
        sendRentalIdsToDeliveryClient(rentalIds);
        return rentalIds;
    }

    private void sendRentalIdsToDeliveryClient(List<Integer> rentalIds) {
        final var deliverRentedFilms = deliveryClient.deliverRentedFilmsReactive(Flux.fromIterable(rentalIds));
        deliverRentedFilms.subscribe(s -> log.info("Delivery Service response: "+s));
    }

    private void savePaymentToDb(Payment payment) {
        final var save = paymentRepository.save(payment);
        if(save.getPaymentId()==null) {
            throw new RuntimeException("Payment Failed!");
        }
        log.info("Payment ID: "+save.getPaymentId());
    }

    private List<Integer> saveRentalsToDb(List<Rental> rentals) {
        final var rentalIds = rentalRepository.saveAll(rentals)
                .stream().map(Rental::getRentalId)
                .collect(Collectors.toList());
        log.info("Rental ids created: "+rentalIds);
        return rentalIds;
    }

    private Staff getStaff() {
        return staffRepository.findById(1).orElseThrow(RuntimeException::new);
    }

    private Customer getCustomer(RentMovieDto rentMovieDto) {
        return customerRepository.findByEmail(rentMovieDto.getCustomerEmail()).orElseThrow(RuntimeException::new);
    }

    private static Function<Film, Publisher<? extends Rental>> filmToRentals(RentMovieDto rentMovieDto, Mono<Customer> customerMono, Mono<Staff> staffMono, Mono<Payment> paymentMono) {
        return f -> Mono.zip(customerMono, paymentMono, staffMono)
                .map(tup3 -> filmToRental(f, rentMovieDto, tup3.getT1(), tup3.getT2(), tup3.getT3()));
    }

    private static Mono<BigDecimal> calculateTotalCostOfRent(RentMovieDto rentMovieDto, Flux<Film> filmList) {
        return filmList.map(film -> film.getRentalRate().multiply(BigDecimal.valueOf(rentMovieDto.getNumberOfDays())))
                .reduce((bigDecimal, bigDecimal2) -> bigDecimal2.add(bigDecimal));
    }

    private static Mono<Payment> getPaymentMono(Mono<BigDecimal> totalCost, Mono<Customer> byEmail, Mono<Staff> byId) {
        return Mono.zip(totalCost, byEmail, byId).map(t3-> getPayment(t3.getT1(), t3.getT2(), t3.getT3()));
    }

    private static Payment getPayment(BigDecimal totalCost, Customer byEmail, Staff byId) {
        final var payment = new Payment();
        payment.setCustomer(byEmail);
        payment.setPaymentDate(currentTimestamp());
        payment.setAmount(totalCost);
        payment.setStaff(byId);
        return payment;
    }

    private static Rental filmToRental(Film film, RentMovieDto rentMovieDto, Customer byEmail, Payment payment, Staff byId) {
        final var rental = new Rental();
        rental.addPayment(payment);
        rental.setInventory(film.getInventories().get(0));
        rental.setCustomer(byEmail);
        rental.setRentalDate(currentTimestamp());
        rental.setLastUpdate(currentTimestamp());
        rental.setReturnDate(new Timestamp(Instant.now().plus(rentMovieDto.getNumberOfDays(), ChronoUnit.DAYS).toEpochMilli()));
        rental.setStaff(byId);
        return rental;
    }

    private static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
