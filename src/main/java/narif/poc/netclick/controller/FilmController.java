package narif.poc.netclick.controller;

import narif.poc.netclick.model.FilmDto;
import narif.poc.netclick.service.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<FilmDto> fetchAllFilms(){
        return filmService.fetchAllFilms();
    }
}
