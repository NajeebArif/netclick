package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("inventory")
public record InventoryRecord(
        @Id
        Integer inventoryId,
        Integer filmId
) {
}
