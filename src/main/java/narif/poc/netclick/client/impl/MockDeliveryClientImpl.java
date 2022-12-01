package narif.poc.netclick.client.impl;

import narif.poc.netclick.client.DeliveryClient;
import narif.poc.netclick.service.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class MockDeliveryClientImpl implements DeliveryClient {

    private final Logger log = LoggerFactory.getLogger(MockDeliveryClientImpl.class);

    private final BlockingQueue<Integer> deliveryQueue = new PriorityBlockingQueue<>();


    @Override
    public String deliverRentedFilms(List<Integer> rentalIds) {
        log.info("Sending rental ids to delivery client: "+rentalIds);
        final var restTemplate = new RestTemplate();
        final var stringResponseEntity = restTemplate.postForEntity(MOCK_PSTMN_IO_DELIVER_FILMS, rentalIds, String.class);
        final var statusCodeValue = stringResponseEntity.getStatusCodeValue();
        log.info("Delivery client response status code: "+statusCodeValue);
        if(statusCodeValue==200){
            final var body = stringResponseEntity.getBody();
            assert Objects.equals(body, "ok");
            log.info("Delivery client response Body: "+body);
            return body;
        }else {
            log.warn("Saving the rental ids to be posted later.");
            deliveryQueue.addAll(rentalIds);
        }
        return "RETRY";
    }

    @Override
    public Mono<String> deliverRentedFilmsReactive(Flux<Integer> rentalIds) {
        throw new UnsupportedOperationException();
    }

}
