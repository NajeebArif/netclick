package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("staff")
public record StaffRecord(
        @Id
        Integer staffId,
        String firstName,
        String lastName,
        Integer addressId,
        String email,
        Integer storeId,
        Boolean active,
        String username,
        String password,
        LocalDateTime lastUpdate
) {
}
