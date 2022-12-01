package narif.poc.netclick.controller;

import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.service.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@RestController
@RequestMapping("rent")
public class RentController {

    private final Logger log = LoggerFactory.getLogger(RentController.class);

    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping
    public Flux<Integer> rentMovies(@RequestBody Mono<RentMovieDto> rentMovieDto){
        log.info("RentController invoked.");
        return rentMovieDto
                .flatMapMany(rentService::rentFilmReactive)
                .log("REST CALL MADE.")
                .subscribeOn(Schedulers.boundedElastic());
    }

}
