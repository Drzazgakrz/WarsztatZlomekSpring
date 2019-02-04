package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.CarData;
import pl.warsztat.zlomek.model.response.CarResponse;
import pl.warsztat.zlomek.service.CarBrandRepository;
import pl.warsztat.zlomek.service.CarRepository;
import pl.warsztat.zlomek.service.ClientRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/car")
public class CarActions {

    private CarBrandRepository carBrandRepository;
    private CarRepository carRepository;
    private CarsHasOwnersRepository carsHasOwnersRepository;
    private ClientRepository clientRepository;

    @Autowired
    public CarActions(CarBrandRepository carBrandRepository, CarRepository carRepository,
                      CarsHasOwnersRepository carsHasOwnersRepository, ClientRepository clientRepository){
        this.carRepository = carRepository;
        this.carBrandRepository = carBrandRepository;
        this.carsHasOwnersRepository = carsHasOwnersRepository;
        this.clientRepository = clientRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CarResponse addCar(@RequestBody CarData carData){
        CarBrand carBrand = carBrandRepository.getCarBrandByName(carData.getCarBrandName());
        Car car = null;
        Client client = clientRepository.findByToken(carData.getAccessToken());
        try {
            car = carRepository.getCarByVin(carData.getVinNumber());
        }catch (ResourcesNotFoundException e){
            car = new Car(carData.getVinNumber(), carData.getModel(), carData.getProdYear(), carBrand);
            carRepository.persistCar(car);
        }
        List<CarsHasOwners> currentOwners = car.getOwners().stream().
                filter((carsHasOwners -> carsHasOwners.getStatus().equals(OwnershipStatus.CURRENT_OWNER))).
                collect(Collectors.toList());
        OwnershipStatus status = (currentOwners.size()!= 0)? OwnershipStatus.NOT_VERIFIED_OWNER:OwnershipStatus.CURRENT_OWNER;
        CarsHasOwners cho = car.addCarOwner(client, status, carData.getRegistrationNumber());
        carsHasOwnersRepository.insertOwnership(cho);
        carRepository.updateCar(car);
        return new CarResponse(car, carData.getAccessToken());
    }
}
