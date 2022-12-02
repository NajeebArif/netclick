package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("customer")
public record CustomerRecord(
        @Id
        Integer customerId,
        Integer storeId,
        String firstName,
        String lastName,
        String email,
        Integer addressId,
        Boolean activebool,
        LocalDate createDate,
        LocalDateTime lastUpdateDate,
        Integer active
) {
}