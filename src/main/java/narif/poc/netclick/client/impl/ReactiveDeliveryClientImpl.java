package narif.poc.netclick.client.impl;

import narif.poc.netclick.client.DeliveryClient;
import narif.poc.netclick.exception.RetryableException;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

import java.time.Duration;
import java.util.List;

@Service
@Primary
public class ReactiveDeliveryClientImpl implements DeliveryClient {

    final WebClient webClient = WebClient.builder().baseUrl(MOCK_PSTMN_IO_DELIVER_FILMS).build();

    @Override
    public Mono<String> deliverRentedFilmsReactive(Flux<Integer> rentalIds) {
        return webClient.post()
                .body(rentalIds, Integer.class)
                .retrieve()
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RetryableException("ERROR While invoking the request")))
                .bodyToMono(String.class)
                .map(String::toUpperCase)
                .retryWhen(getRetryBackoffSpec())
                .log("Making rest call: ")
                .subscribeOn(Schedulers.boundedElastic());
    }

    private static RetryBackoffSpec getRetryBackoffSpec() {
        return Retry.backoff(3, Duration.ofSeconds(3))
                .jitter(0.75)
                .filter(throwable -> throwable instanceof RetryableException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new RuntimeException("Failed to post rental ids to delivery client.");
                });
    }

    @Override
    public String deliverRentedFilms(List<Integer> rentalIds) {
        throw new UnsupportedOperationException();
    }
}
