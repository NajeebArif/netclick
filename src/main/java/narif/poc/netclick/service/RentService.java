package narif.poc.netclick.service;

import narif.poc.netclick.client.DeliveryClient;
import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.model.entity.*;
import narif.poc.netclick.repository.CustomerRepository;
import narif.poc.netclick.repository.PaymentRepository;
import narif.poc.netclick.repository.RentalRepository;
import narif.poc.netclick.repository.StaffRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public List<Integer> rentFilm(RentMovieDto rentMovieDto) {
        final var filmList = filmService.findAllByIds(rentMovieDto.getFilmIds());
        final var totalCost = calculateTotalCostOfRent(rentMovieDto, filmList).orElseThrow(RuntimeException::new);
        final var customer = customerRepository.findByEmail(rentMovieDto.getCustomerEmail()).orElseThrow(RuntimeException::new);
        final var staff = staffRepository.findById(1).orElseThrow(RuntimeException::new);
        final Payment payment = getPayment(totalCost, customer, staff);
        final var rentals = filmList.stream().map(getFilmRentalFunction(rentMovieDto, customer, payment, staff)).collect(Collectors.toList());
        for (Rental ren : rentals) {
            payment.setRental(ren);
        }
        final var rentalIds = rentalRepository.saveAll(rentals).stream().map(Rental::getRentalId).collect(Collectors.toList());
        final var save = paymentRepository.save(payment);
        if(save.getPaymentId()==null)
            throw new RuntimeException("Payment Failed!");
        deliveryClient.deliverRentedFilms(rentalIds);
        return rentalIds;
    }

    private static Optional<BigDecimal> calculateTotalCostOfRent(RentMovieDto rentMovieDto, List<Film> filmList) {
        return filmList.stream().map(film -> film.getRentalRate().multiply(BigDecimal.valueOf(rentMovieDto.getNumberOfDays())))
                .reduce((bigDecimal, bigDecimal2) -> bigDecimal2.add(bigDecimal));
    }

    private static Payment getPayment(BigDecimal totalCost, Customer byEmail, Staff byId) {
        final var payment = new Payment();
        payment.setCustomer(byEmail);
        payment.setPaymentDate(currentTimestamp());
        payment.setAmount(totalCost);
        payment.setStaff(byId);
        return payment;
    }

    private static Function<Film, Rental> getFilmRentalFunction(RentMovieDto rentMovieDto, Customer byEmail, Payment payment, Staff byId) {
        return film -> {
            final var rental = new Rental();
            rental.addPayment(payment);
            rental.setInventory(film.getInventories().get(0));
            rental.setCustomer(byEmail);
            rental.setRentalDate(currentTimestamp());
            rental.setLastUpdate(currentTimestamp());
            rental.setReturnDate(new Timestamp(Instant.now().plus(rentMovieDto.getNumberOfDays(), ChronoUnit.DAYS).toEpochMilli()));
            rental.setStaff(byId);
            return rental;
        };
    }

    private static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
