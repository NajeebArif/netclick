package narif.poc.netclick.service;

import narif.poc.netclick.model.FilmDto;
import narif.poc.netclick.model.entity.Film;
import narif.poc.netclick.repository.FilmRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<FilmDto> fetchAllFilms(){
        final var all = filmRepository.findAll();
        return all.stream().map(filmToFilmDtoMapper()).collect(Collectors.toList());
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
