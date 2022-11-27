package narif.poc.netclick.model;

import java.util.List;

public class RentMovieDto {
    private String customerEmail;
    private Integer numberOfDays;
    private List<Integer> filmIds;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public Integer getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Integer numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public List<Integer> getFilmIds() {
        return filmIds;
    }

    public void setFilmIds(List<Integer> filmIds) {
        this.filmIds = filmIds;
    }
}
