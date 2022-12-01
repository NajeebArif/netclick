package narif.poc.netclick.client.impl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import static narif.poc.netclick.client.DeliveryClient.MOCK_PSTMN_IO_DELIVER_FILMS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReactiveDeliveryClientImplTest {

    @Test
    @Disabled("Don't do this at all")
    void testWebclient(){
        Flux<Integer> rentalIds = Flux.just(1,2,3,4,5,6);
        final WebClient webClient = WebClient.builder().baseUrl(MOCK_PSTMN_IO_DELIVER_FILMS).build();
        final var block = webClient.post()
                .body(rentalIds, Integer.class)
                .retrieve()
                .bodyToMono(String.class);
        assertThat(block).isEqualTo("ok");
    }
}