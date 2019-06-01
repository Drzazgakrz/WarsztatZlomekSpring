package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.warsztat.zlomek.model.db.CarsHasOwners;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.OwnershipStatus;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class Car {
    protected String carBrandName;
    protected String model;
    protected int prodYear;
    protected String vinNumber;
    protected String registrationNumber;

    public Car(pl.warsztat.zlomek.model.db.Car car, Client client){
        this.carBrandName = car.getBrand().getBrandName();
        this.model = car.getModel();
        this.vinNumber = car.getVin();
        List<CarsHasOwners> carsHasOwners = car.getOwners().stream().filter((cho)->
                cho.getOwner().equals(client)).collect(Collectors.toList());
        this.registrationNumber = carsHasOwners.get(0).getRegistrationNumber();
        this.prodYear = car.getProdYear();
    }

    public Car(pl.warsztat.zlomek.model.db.Car car){
        this.carBrandName = car.getBrand().getBrandName();
        this.model = car.getModel();
        this.vinNumber = car.getVin();
        this.registrationNumber = null;
        this.prodYear = car.getProdYear();
    }
}
