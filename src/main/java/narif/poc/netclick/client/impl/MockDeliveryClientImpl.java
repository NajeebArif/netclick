package narif.poc.netclick.client.impl;

import narif.poc.netclick.client.DeliveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Service
public class MockDeliveryClientImpl implements DeliveryClient {

    public static final String MOCK_PSTMN_IO_DELIVER_FILMS = "https://4d0c5a98-5307-44ff-beda-dde64abe14a9.mock.pstmn.io/deliverFilms";
    private final BlockingQueue<Integer> deliveryQueue = new PriorityBlockingQueue<>();


    @Override
    public void deliverRentedFilms(List<Integer> rentalIds) {
        final var restTemplate = new RestTemplate();
        final var stringResponseEntity = restTemplate.postForEntity(MOCK_PSTMN_IO_DELIVER_FILMS, rentalIds, String.class);
        final var statusCodeValue = stringResponseEntity.getStatusCodeValue();
        if(statusCodeValue==200){
            final var body = stringResponseEntity.getBody();
            assert Objects.equals(body, "ok");
        }else {
            deliveryQueue.addAll(rentalIds);
        }
    }

}
