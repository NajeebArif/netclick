package narif.poc.netclick.repository.jpa;

import narif.poc.netclick.config.NonReactiveTypes;
import narif.poc.netclick.model.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@NonReactiveTypes
public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("select f from Film f join f.filmActors fa where fa.actor.firstName = :firstName")
    Page<Film> findAllFilmsForActor(String firstName, Pageable pageable);

    @Query("select distinct f from Film f join f.filmActors fa where " +
            "f.title like %:title% or " +
            "concat(lower(fa.actor.firstName), ' ',lower(fa.actor.lastName)) like %:actorName%")
    Page<Film> searchMovies(String title, String actorName, Pageable pageable);

    @Query("select distinct f from Film f join f.filmActors fa where " +
            "f.title like %:title%")
    Page<Film> searchMoviesForTitle(String title, Pageable pageable);

    @Query("select distinct f from Film f join f.filmActors fa where " +
            "concat(lower(fa.actor.firstName), ' ',lower(fa.actor.lastName)) like %:actorName%")
    Page<Film> searchMoviesForActorNames(String actorName, Pageable pageable);

    @Query("select f from Film f join f.filmCategories fc where fc.category.name in (:genres)")
    Page<Film> findAllForGenres(List<String> genres, Pageable pageable);

    List<Film> findAllByFilmIdIn(List<Integer> filmIds);
}
