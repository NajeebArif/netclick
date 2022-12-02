package narif.poc.netclick.repository.jpa;

import narif.poc.netclick.config.NonReactiveTypes;
import narif.poc.netclick.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@NonReactiveTypes
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
