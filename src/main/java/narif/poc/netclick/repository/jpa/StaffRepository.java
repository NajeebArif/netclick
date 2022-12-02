package narif.poc.netclick.repository.jpa;

import narif.poc.netclick.config.NonReactiveTypes;
import narif.poc.netclick.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@NonReactiveTypes
public interface StaffRepository extends JpaRepository<Staff, Integer> {
}
