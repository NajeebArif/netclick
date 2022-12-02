package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("payment")
public record PaymentRecord(
        @Id
        Integer paymentId,
        Integer customerId,
        Integer staffId,
        Integer rentalId,
        Double amount,
        LocalDateTime paymentDate
) {
        public static PaymentRecord of(Integer customerId, Integer staffId, Integer rentalId, Double amount){
                return new PaymentRecord(null, customerId, staffId, rentalId, amount, LocalDateTime.now());
        }
}
