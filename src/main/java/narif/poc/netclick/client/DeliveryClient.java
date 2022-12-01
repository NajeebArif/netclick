package narif.poc.netclick.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DeliveryClient {

    String MOCK_PSTMN_IO_DELIVER_FILMS = "https://4d0c5a98-5307-44ff-beda-dde64abe14a9.mock.pstmn.io/deliverFilms";

    String deliverRentedFilms(List<Integer> rentalIds);

    Mono<String> deliverRentedFilmsReactive(Flux<Integer> rentalIds);
}
