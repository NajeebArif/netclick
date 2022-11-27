package narif.poc.netclick.repository;

import narif.poc.netclick.model.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("select f from Film f join f.filmActors fa where fa.actor.firstName = :firstName")
    Page<Film> findAllFilmsForActor(String firstName, Pageable pageable);

    @Query("select f from Film f join f.filmCategories fc where fc.category.name in (:genres)")
    Page<Film> searchMovies(List<String> genres, Pageable pageable);
}
