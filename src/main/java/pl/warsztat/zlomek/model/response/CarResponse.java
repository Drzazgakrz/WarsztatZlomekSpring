package pl.warsztat.zlomek.model.response;

import pl.warsztat.zlomek.model.db.Car;
import pl.warsztat.zlomek.model.db.CarsHasOwners;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.OwnershipStatus;
import pl.warsztat.zlomek.model.request.CarData;

import java.util.List;
import java.util.stream.Collectors;


public class CarResponse extends CarData {
    private long carId;

    public CarResponse(Car car, String accessToken, Client client){
        super(car, accessToken, client);
        this.carId = car.getId();
        this.carBrandName = car.getBrand().getBrandName();
        this.model = car.getModel();
        this.vinNumber = car.getVin();
        List<CarsHasOwners> carsHasOwners = car.getOwners().stream().filter((cho)->
                cho.getStatus().equals(OwnershipStatus.CURRENT_OWNER)||cho.getStatus().equals(OwnershipStatus.COOWNER))
                .collect(Collectors.toList());
        this.registrationNumber = carsHasOwners.get(0).getRegistrationNumber();
    }
}
