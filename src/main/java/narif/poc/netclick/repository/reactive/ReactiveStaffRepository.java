package narif.poc.netclick.repository.reactive;

import narif.poc.netclick.model.records.StaffRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactiveStaffRepository extends ReactiveCrudRepository<StaffRecord, Integer> {
}
