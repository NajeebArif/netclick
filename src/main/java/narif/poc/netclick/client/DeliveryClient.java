package narif.poc.netclick.client;

import java.util.List;

public interface DeliveryClient {

    void deliverRentedFilms(List<Integer> rentalIds);
}
