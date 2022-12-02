package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.CustomerRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveCustomerRepository extends ReactiveCrudRepository<CustomerRecord, Integer> {
    Mono<CustomerRecord> findByEmail(String customerEmail);
}
