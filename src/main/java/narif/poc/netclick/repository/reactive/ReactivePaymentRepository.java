package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.PaymentRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactivePaymentRepository extends ReactiveCrudRepository<PaymentRecord, Integer> {
}
