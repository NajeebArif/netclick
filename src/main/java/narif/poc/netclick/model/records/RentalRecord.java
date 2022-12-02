package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("rental")
public record RentalRecord(
        @Id
        Integer rentalId,
        LocalDateTime rentalDate,
        Integer inventoryId,
        Integer customerId,
        LocalDateTime returnDate,
        Integer staffId,
        LocalDateTime lastUpdate
) {
        public static RentalRecord of(Integer inventoryId, Integer customerId, Integer staffId){
                return new RentalRecord(null, LocalDateTime.now(), inventoryId, customerId,
                        LocalDateTime.now(), staffId, LocalDateTime.now());
        }
}
