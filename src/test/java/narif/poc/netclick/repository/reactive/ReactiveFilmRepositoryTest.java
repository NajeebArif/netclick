package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.InventoryRecord;
import narif.poc.netclick.model.records.PaymentRecord;
import narif.poc.netclick.model.records.RentalRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
class ReactiveFilmRepositoryTest {

    @Autowired
    private ReactiveFilmRepository reactiveFilmRepository;
    @Autowired
    private ReactiveRentalRepository reactiveRentalRepository;

    @Autowired
    private ReactivePaymentRepository reactivePaymentRepository;

    @Autowired
    private ReactiveInventoryRepository reactiveInventoryRepository;

    @Test
    public void testSave(){
        final var of = RentalRecord.of(1, 1, 1);
        final var block = reactiveRentalRepository.save(of)
                .subscribeOn(Schedulers.boundedElastic()).block();
        System.out.println(block);

        final var of1 = PaymentRecord.of(1, 1, 1, 1.9);
        final var block1 = reactivePaymentRepository.save(of1).block();
        System.out.println(block1);


    }

    @Test
    void testInven(){
        final var allByFilmId = reactiveInventoryRepository.findAllByFilmId(Mono.just(1)).collectList().block();
        System.out.println(allByFilmId);

        final var block = reactiveInventoryRepository.findAllByFilmId(1).take(1).collectList().block();
        System.out.println(block);
    }

    @Test
    @DisplayName("given an id of 1, should return the film with title")
    void testFindById(){
        final var byId = reactiveFilmRepository.findById(1);
        StepVerifier.create(byId)
                .expectNextMatches(filmRecord -> filmRecord.title().equalsIgnoreCase("Academy Dinosaur"))
                .expectComplete();
    }

}