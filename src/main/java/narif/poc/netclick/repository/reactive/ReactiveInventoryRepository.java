package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.InventoryRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveInventoryRepository extends ReactiveCrudRepository<InventoryRecord, Integer> {
    Flux<InventoryRecord> findAllByFilmId(Mono<Integer> filmIdMono);
    Flux<InventoryRecord> findAllByFilmId(Integer filmId);
}
