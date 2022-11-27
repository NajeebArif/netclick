package narif.poc.netclick.controller;

import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.model.entity.Rental;
import narif.poc.netclick.service.RentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rent")
public class RentController {

    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public List<Integer> rentMovies(@RequestBody RentMovieDto rentMovieDto){
        return rentService.rentFilm(rentMovieDto);
    }

}
