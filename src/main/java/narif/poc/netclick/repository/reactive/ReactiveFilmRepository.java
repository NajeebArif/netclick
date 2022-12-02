package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.FilmRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveFilmRepository extends ReactiveCrudRepository<FilmRecord, Integer> {
}
