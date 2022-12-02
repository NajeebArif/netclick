package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.FilmRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReactiveFilmRepositoryTest {

    @Autowired
    private ReactiveFilmRepository reactiveFilmRepository;

    @Test
    void testFindAll(){
        final var byId = reactiveFilmRepository.findById(1);
        StepVerifier.create(byId)
                .expectNextMatches(filmRecord -> filmRecord.title().equalsIgnoreCase("Academy Dinosaur"))
                .expectComplete();
    }

}