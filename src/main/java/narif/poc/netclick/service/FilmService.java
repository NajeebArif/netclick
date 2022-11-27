package narif.poc.netclick.service;

import narif.poc.netclick.model.FilmDto;
import narif.poc.netclick.model.FilmSearchQueryDto;
import narif.poc.netclick.model.entity.Film;
import narif.poc.netclick.repository.FilmRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public Page<FilmDto> fetchAllFilms(PageRequest pageRequest) {
        return filmRepository.findAll(pageRequest)
                .map(filmToFilmDtoMapper());
    }

    public Page<FilmDto> findMoviesForActorFirstName(FilmSearchQueryDto filmSearchQueryDto, PageRequest of) {
        return filmRepository.findAllFilmsForActor(filmSearchQueryDto.getActor(), of)
                .map(filmToFilmDtoMapper());
    }

    public Page<FilmDto> searchMovies(FilmSearchQueryDto filmSearchQueryDto, PageRequest of) {
        if (isValidTitle(filmSearchQueryDto) && isValidActorsName(filmSearchQueryDto))
            return filmRepository.searchMovies(filmSearchQueryDto.getTitle(), filmSearchQueryDto.getActor(), of)
                    .map(filmToFilmDtoMapper());
        else if (isValidTitle(filmSearchQueryDto)) {
            return filmRepository.searchMoviesForTitle(filmSearchQueryDto.getTitle(), of)
                    .map(filmToFilmDtoMapper());
        } else if (isValidActorsName(filmSearchQueryDto)) {
            return filmRepository.searchMoviesForTitle(filmSearchQueryDto.getActor(), of)
                    .map(filmToFilmDtoMapper());
        } else
            throw new RuntimeException("invalid request");
    }

    private static boolean isValidActorsName(FilmSearchQueryDto filmSearchQueryDto) {
        return filmSearchQueryDto.getActor() != null && !filmSearchQueryDto.getActor().isEmpty();
    }

    private static boolean isValidTitle(FilmSearchQueryDto filmSearchQueryDto) {
        return filmSearchQueryDto.getTitle() != null && !filmSearchQueryDto.getTitle().isEmpty();
    }

    public Page<FilmDto> searchMoviesByGenre(List<String> genres, Pageable pageable) {
        return filmRepository.findAllForGenres(genres, pageable).map(filmToFilmDtoMapper());
    }

    private static Function<Film, FilmDto> filmToFilmDtoMapper() {
        return film -> {
            final var filmDto = new FilmDto();
            BeanUtils.copyProperties(film, filmDto);
            filmDto.setLanguage(film.getLanguage().getName());
            filmDto.setLastUpdate(film.getLastUpdate().toLocalDateTime());
            filmDto.setRentalRate(film.getRentalRate().doubleValue());
            filmDto.setReplacementCost(film.getReplacementCost().doubleValue());
            return filmDto;
        };
    }
}
