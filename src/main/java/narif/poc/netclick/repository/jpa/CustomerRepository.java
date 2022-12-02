package narif.poc.netclick.repository.jpa;

import narif.poc.netclick.config.NonReactiveTypes;
import narif.poc.netclick.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@NonReactiveTypes
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmail(String email);
}
