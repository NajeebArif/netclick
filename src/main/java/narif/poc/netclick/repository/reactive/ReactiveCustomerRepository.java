package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.CustomerRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveCustomerRepository extends ReactiveCrudRepository<CustomerRecord, Integer> {
}
