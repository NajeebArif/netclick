package narif.poc.netclick.model.records;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("film")
public record FilmRecord(
        @Id
        Integer filmId,
        String title,
        String description,
        Integer releaseYear,
        Integer languageId,
        Integer rentalDuration,
        Double rentalRate,
        Integer length,
        Double replacementCost,
        String rating,
        LocalDateTime lastUpdate
) {
}
