package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.CarRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Car;
import pl.warsztat.zlomek.model.db.CarsHasOwners;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.OwnershipStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository){
        this.carRepository = carRepository;
    }

    public Car getClientCar(Client client, long id){
        Car car = carRepository.getCarById(id);
        List<CarsHasOwners> currentOwners = car.getOwners().stream()
                .filter(carsHasOwners -> (carsHasOwners.getStatus().equals(OwnershipStatus.CURRENT_OWNER)
                        || carsHasOwners.getStatus().equals(OwnershipStatus.COOWNER))
                        && carsHasOwners.getOwner().equals(client))
                .collect(Collectors.toList());
        if(currentOwners.size() == 0)
            throw new ResourcesNotFoundException("Samochód nie należy do tego klienta");
        return car;
    }
}
