package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.RentalRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveRentalRepository extends ReactiveCrudRepository<RentalRecord, Integer> {
}
