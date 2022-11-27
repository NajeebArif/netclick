package narif.poc.netclick.controller;

import narif.poc.netclick.model.FilmDto;
import narif.poc.netclick.model.FilmSearchQueryDto;
import narif.poc.netclick.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Page<FilmDto> fetchAllFilms(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "20") int size){
        final var of = PageRequest.of(page, size, Sort.by("filmId").ascending());
        return filmService.fetchAllFilms(of);
    }

    @PostMapping("/search")
    public Page<FilmDto> searchFilms(@RequestBody FilmSearchQueryDto filmSearchQueryDto,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size){
        final var of = PageRequest.of(page, size, Sort.by("filmId").ascending());
        return filmService.searchMovies(filmSearchQueryDto, of);
    }

    @PostMapping("/genres")
    public Page<FilmDto> searchFilmsForGenre(@RequestBody FilmSearchQueryDto filmSearchQueryDto,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size){
        final var of = PageRequest.of(page, size, Sort.by("filmId").ascending());
        final var genres = Arrays.stream(filmSearchQueryDto.getGenre().split(",")).collect(Collectors.toList());
        return filmService.searchMoviesByGenre(genres, of);
    }
}
