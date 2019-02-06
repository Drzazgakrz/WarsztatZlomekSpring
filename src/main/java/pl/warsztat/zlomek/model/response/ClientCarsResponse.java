package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import lombok.Setter;
import pl.warsztat.zlomek.model.request.Car;

import java.util.List;

@Getter
@Setter
public class ClientCarsResponse {
    private List<Car> cars;
    private String accessToken;

    public ClientCarsResponse(List<Car> cars, String accessToken) {
        this.cars = cars;
        this.accessToken = accessToken;
    }


}
