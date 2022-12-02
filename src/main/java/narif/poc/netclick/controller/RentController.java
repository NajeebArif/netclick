package narif.poc.netclick.controller;

import narif.poc.netclick.model.RentMovieDto;
import narif.poc.netclick.service.ReactiveRentService;
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

@RestController
@RequestMapping("rent")
public class RentController {

    private final Logger log = LoggerFactory.getLogger(RentController.class);

    private final RentService rentService;
    private final ReactiveRentService reactiveRentService;

    public RentController(RentService rentService, ReactiveRentService reactiveRentService) {
        this.rentService = rentService;
        this.reactiveRentService = reactiveRentService;
    }

    @PostMapping
    public Flux<Integer> rentMovies(@RequestBody Mono<RentMovieDto> rentMovieDto){
        log.info("RentController invoked.");
        return rentMovieDto
                .flatMapMany(rentService::rentFilm)
                .log("REST CALL MADE.")
                .subscribeOn(Schedulers.boundedElastic());
    }

    @PostMapping("reactive")
    public Flux<Integer> rentMoviesReactive(@RequestBody Mono<RentMovieDto> rentMovieDto){
        log.info("RentController invoked.");
        return rentMovieDto
                .flatMapMany(reactiveRentService::rentFilm)
                .log("REST CALL MADE.")
                .subscribeOn(Schedulers.boundedElastic());
    }



}
