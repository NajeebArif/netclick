package narif.poc.netclick.client;

import java.util.List;

public interface DeliveryClient {

    String deliverRentedFilms(List<Integer> rentalIds);
}
