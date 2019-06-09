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
    protected long id;
    protected String brandName;
    protected String model;
    protected int productionYear;
    protected String vin;
    protected String registrationNumber;

    public Car(pl.warsztat.zlomek.model.db.Car car, Client client){
        this.id = car.getId();
        this.brandName = car.getBrand().getBrandName();
        this.model = car.getModel();
        this.vin = car.getVin();
        List<CarsHasOwners> carsHasOwners = car.getOwners().stream().filter((cho)->
                cho.getOwner().equals(client)).collect(Collectors.toList());
        this.registrationNumber = carsHasOwners.get(0).getRegistrationNumber();
        this.productionYear = car.getProdYear();
    }

    public Car(pl.warsztat.zlomek.model.db.Car car){
        this.id = car.getId();
        this.brandName = car.getBrand().getBrandName();
        this.model = car.getModel();
        this.vin = car.getVin();
        this.registrationNumber = null;
        this.productionYear = car.getProdYear();
    }
}
