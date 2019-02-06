package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.service.CarService;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.CarData;
import pl.warsztat.zlomek.model.response.CarResponse;
import pl.warsztat.zlomek.model.response.ClientCarsResponse;
import pl.warsztat.zlomek.data.CarBrandRepository;
import pl.warsztat.zlomek.data.CarRepository;
import pl.warsztat.zlomek.data.CarsHasOwnersRepository;
import pl.warsztat.zlomek.data.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/car")
public class CarsController {

    private CarBrandRepository carBrandRepository;
    private CarRepository carRepository;
    private CarsHasOwnersRepository carsHasOwnersRepository;
    private ClientRepository clientRepository;
    private CarService carService;

    @Autowired
    public CarsController(CarBrandRepository carBrandRepository, CarRepository carRepository, CarService carService,
                          CarsHasOwnersRepository carsHasOwnersRepository, ClientRepository clientRepository){
        this.carRepository = carRepository;
        this.carBrandRepository = carBrandRepository;
        this.carsHasOwnersRepository = carsHasOwnersRepository;
        this.clientRepository = clientRepository;
        this.carService = carService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CarResponse addCar(@RequestBody CarData carData){
        CarBrand carBrand = carBrandRepository.getCarBrandByName(carData.getCarBrandName());
        Car car;
        Client client = clientRepository.findByToken(carData.getAccessToken());
        try {
            car = carRepository.getCarByVin(carData.getVinNumber());
        }catch (ResourcesNotFoundException e){
            car = new Car(carData.getVinNumber(), carData.getModel(), carData.getProdYear(), carBrand);
            carRepository.persistCar(car);
        }
        List<CarsHasOwners> currentOwners = car.getOwners().stream()
                .filter((carsHasOwners -> carsHasOwners.getStatus().equals(OwnershipStatus.CURRENT_OWNER) ||
                                carsHasOwners.getStatus().equals(OwnershipStatus.COOWNER))).collect(Collectors.toList());
        OwnershipStatus status = currentOwners.size()!= 0 ? OwnershipStatus.NOT_VERIFIED_OWNER:OwnershipStatus.CURRENT_OWNER;
        CarsHasOwners cho = car.addCarOwner(client, status, carData.getRegistrationNumber());
        carsHasOwnersRepository.insertOwnership(cho);
        carRepository.updateCar(car);
        return new CarResponse(car, carData.getAccessToken(), client);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public CarResponse getCar(@PathVariable long id, @RequestHeader String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        return new CarResponse(carService.getClientCar(client, id), accessToken, client);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ClientCarsResponse getClientsCars(@RequestHeader String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        List<pl.warsztat.zlomek.model.request.Car> cars = new ArrayList<>();
        client.getCars().forEach(carsHasOwners -> cars.add(new pl.warsztat.zlomek.model.request.Car(carsHasOwners.getCar(), client)));
        return new ClientCarsResponse(cars, accessToken);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AccessTokenModel removeCar(@PathVariable long id, @RequestHeader String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        Car car = carService.getClientCar(client, id);
        List<CarsHasOwners> cars = client.getCars().stream().filter(cho->cho.getCar().equals(car)).collect(Collectors.toList());
        if(cars.size()==0){
            throw new ResourcesNotFoundException("Klient nie posiada takiego samochodu");
        }
        cars.get(0).setStatus(OwnershipStatus.FORMER_OWNER);
        carsHasOwnersRepository.updateOwnership(cars.get(0));
        return new AccessTokenModel(accessToken);
    }
}
