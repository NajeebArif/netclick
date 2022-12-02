package narif.poc.netclick.repository.reactive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class ReactiveFilmRepositoryTest {

    @Autowired
    private ReactiveFilmRepository reactiveFilmRepository;

    @Test
    @DisplayName("given an id of 1, should return the film with title")
    void testFindById(){
        final var byId = reactiveFilmRepository.findById(1);
        StepVerifier.create(byId)
                .expectNextMatches(filmRecord -> filmRecord.title().equalsIgnoreCase("Academy Dinosaur"))
                .expectComplete();
    }

}