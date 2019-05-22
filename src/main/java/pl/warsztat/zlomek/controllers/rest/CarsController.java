package pl.warsztat.zlomek.controllers.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.model.request.AddCarToCompanyModel;
import pl.warsztat.zlomek.model.request.AddCoownerRequest;
import pl.warsztat.zlomek.service.CarService;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.CarData;
import pl.warsztat.zlomek.model.response.CarResponse;
import pl.warsztat.zlomek.model.response.ClientCarsResponse;
import pl.warsztat.zlomek.service.CompanyService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/rest/car")
public class CarsController {

    private CarBrandRepository carBrandRepository;
    private CarRepository carRepository;
    private CarsHasOwnersRepository carsHasOwnersRepository;
    private ClientRepository clientRepository;
    private CarService carService;
    private CompaniesRepository companiesRepository;
    private CompaniesHasCarsRepository companiesHasCarsRepository;
    private CompanyService companyService;
    private Logger log;

    @Autowired
    public CarsController(CarBrandRepository carBrandRepository, CarRepository carRepository, CarService carService,
                          CarsHasOwnersRepository carsHasOwnersRepository, ClientRepository clientRepository,
                          CompaniesRepository companiesRepository, CompaniesHasCarsRepository companiesHasCarsRepository,
                          CompanyService companyService, Logger log){
        this.carRepository = carRepository;
        this.carBrandRepository = carBrandRepository;
        this.carsHasOwnersRepository = carsHasOwnersRepository;
        this.clientRepository = clientRepository;
        this.carService = carService;
        this.companiesRepository = companiesRepository;
        this.companiesHasCarsRepository = companiesHasCarsRepository;
        this.companyService = companyService;
        this.log = log;
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
        carService.addOwnership(car, client, status, carData.getRegistrationNumber());
        carRepository.updateCar(car);
        return new CarResponse(car, carData.getAccessToken(), client);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}")
    public CarResponse getCar(@PathVariable long id, @RequestBody AccessTokenModel accessToken){
        Client client = clientRepository.findByToken(accessToken.getAccessToken());
        return new CarResponse(carService.getClientCar(client, id).getCar(), accessToken.getAccessToken(), client);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/getClientsCars")
    public ClientCarsResponse getClientsCars(@RequestHeader String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        List<pl.warsztat.zlomek.model.request.Car> cars = new ArrayList<>();
        client.getCars().forEach(carsHasOwners ->
                cars.add(new pl.warsztat.zlomek.model.request.Car(carsHasOwners.getCar(), client)));
        return new ClientCarsResponse(cars, accessToken);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public AccessTokenModel removeCar(@PathVariable long id, @RequestBody AccessTokenModel accessToken){
        Client client = clientRepository.findByToken(accessToken.getAccessToken());
        Car car = carService.getClientCar(client, id).getCar();
        CarsHasOwners cho = client.getCars().stream().filter(ownership->
                ownership.getCar().equals(car)).findAny().orElse(null);
        if(cho == null){
            throw new ResourcesNotFoundException("Klient nie posiada takiego samochodu", accessToken.getAccessToken());
        }
        cho.setStatus(OwnershipStatus.FORMER_OWNER);
        carsHasOwnersRepository.updateOwnership(cho);
        return accessToken;
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/addCarToCompany")
    public AccessTokenModel addCarToCompany(@RequestBody AddCarToCompanyModel addCarToCompanyModel){
        Client client = clientRepository.findByToken(addCarToCompanyModel.getAccessToken());
        Car car = carService.getClientCar(client, addCarToCompanyModel.getCarId()).getCar();
        Company company = companyService.getClientCompany(client, addCarToCompanyModel.getCompanyId());
        CompaniesHasCars chc = company.addCar(car);
        car.addCompany(chc);
        this.carRepository.updateCar(car);
        this.companiesHasCarsRepository.saveCompaniesCarsRelationship(chc);
        return new AccessTokenModel(addCarToCompanyModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addCoowner")
    public AccessTokenModel addCoowner(@RequestBody AddCoownerRequest request){
        Client client = this.clientRepository.findByToken(request.getAccessToken());
        CarsHasOwners cho = carService.getClientCar(client, request.getCarId());
        if(cho == null)
            throw new ResourcesNotFoundException("Samochód nie należy do tego klienta");
        cho.setStatus(OwnershipStatus.COOWNER);
        this.carsHasOwnersRepository.updateOwnership(cho);
        Arrays.stream(request.getNewCoowners()).forEach(username->{
            try {
                Client coowner = this.clientRepository.findClientByUsername(username);
                this.carService.addOwnership(cho.getCar(), coowner, OwnershipStatus.COOWNER, cho.getRegistrationNumber());
            }catch (Exception e){}
        });
        this.carRepository.updateCar(cho.getCar());
        return new AccessTokenModel(request.getAccessToken());
    }
}