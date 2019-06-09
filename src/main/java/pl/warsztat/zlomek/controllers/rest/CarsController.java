package pl.warsztat.zlomek.controllers.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.model.request.AddCarToCompanyModel;
import pl.warsztat.zlomek.model.request.AddCoownerRequest;
import pl.warsztat.zlomek.model.response.CarBrandResponse;
import pl.warsztat.zlomek.model.response.CarBrandsList;
import pl.warsztat.zlomek.model.db.Car;
import pl.warsztat.zlomek.model.request.*;
import pl.warsztat.zlomek.service.CarService;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.response.CarResponse;
import pl.warsztat.zlomek.model.response.ClientCarsResponse;
import pl.warsztat.zlomek.service.CompanyService;

import javax.validation.Valid;
import java.time.LocalDate;
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
    private EmployeeRepository employeeRepository;
    private Logger log;

    @Autowired
    public CarsController(CarBrandRepository carBrandRepository, CarRepository carRepository, CarService carService,
                          CarsHasOwnersRepository carsHasOwnersRepository, ClientRepository clientRepository,
                          CompaniesRepository companiesRepository, CompaniesHasCarsRepository companiesHasCarsRepository,
                          CompanyService companyService, Logger log, EmployeeRepository employeeRepository){
        this.carRepository = carRepository;
        this.carBrandRepository = carBrandRepository;
        this.carsHasOwnersRepository = carsHasOwnersRepository;
        this.clientRepository = clientRepository;
        this.carService = carService;
        this.companiesRepository = companiesRepository;
        this.companiesHasCarsRepository = companiesHasCarsRepository;
        this.companyService = companyService;
        this.employeeRepository = employeeRepository;
        this.log = log;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CarResponse addCar(@RequestBody CarData carData){
        CarBrand carBrand = carBrandRepository.getCarBrandByName(carData.getBrandName());
        Car car;
        Client client = clientRepository.findByToken(carData.getAccessToken());
        try {
            car = carRepository.getCarByVin(carData.getVin());
        }catch (ResourcesNotFoundException e){
            car = new Car(carData.getVin(), carData.getModel(), carData.getProductionYear(), carBrand);
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
    public ClientCarsResponse getClientsCars(@RequestBody AccessTokenModel accessToken){
        Client client = clientRepository.findByToken(accessToken.getAccessToken());
        List<pl.warsztat.zlomek.model.request.Car> cars = new ArrayList<>();
        client.getCars().forEach(carsHasOwners ->
                cars.add(new pl.warsztat.zlomek.model.request.Car(carsHasOwners.getCar(), client)));
        return new ClientCarsResponse(cars, accessToken.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getCarBrands")
    public CarBrandsList getCarBrands(){
        List<CarBrand> brands = carBrandRepository.getCarBrands();
        ArrayList<CarBrandResponse> brandsResponse = new ArrayList<>();
        brands.forEach(brand -> brandsResponse.add(new CarBrandResponse(brand)));

        return new CarBrandsList(brandsResponse);
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
        this.companiesHasCarsRepository.saveCompaniesCarsRelationship(chc);
        this.carRepository.updateCar(car);
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
                Client coowner = this.clientRepository.findClientByUsername(request.getCoownerUsername());
                this.carService.addOwnership(cho.getCar(), coowner, OwnershipStatus.COOWNER, cho.getRegistrationNumber());
        this.carRepository.updateCar(cho.getCar());
        return new AccessTokenModel(request.getAccessToken());
    }

    @PostMapping(path = "removeCoowner")
    public AccessTokenModel removeCoowner(@RequestBody AddCoownerRequest request){
        Client client = this.clientRepository.findByToken(request.getAccessToken());
        CarsHasOwners cho = this.carService.getClientCar(client, request.getCarId());

        this.carService.setCoownersStatus(request.getCoownerUsername(), request.getCarId());

        Car car = cho.getCar();
        List<CarsHasOwners> choList = car.getOwners().stream()
                .filter(current-> current.getStatus().equals(OwnershipStatus.COOWNER)).collect(Collectors.toList());
        if(choList.size() == 1){
            cho.setStatus(OwnershipStatus.CURRENT_OWNER);
            this.carsHasOwnersRepository.updateOwnership(cho);
        }
        this.carRepository.updateCar(car);
        return new AccessTokenModel(request.getAccessToken());
    }

    @PostMapping(path = "verifyOwnership")
    public AccessTokenModel verifyOwnership(@RequestBody VerifyOwnershipRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        this.carService.verifyOwnership(request);
        return new AccessTokenModel(request.getAccessToken());
    }

    @PostMapping(path = "carBrand")
    public AccessTokenModel addCarBrand(@RequestBody AddCarBrandModel carBrandModel){
        this.employeeRepository.findByToken(carBrandModel.getAccessToken());
        CarBrand carBrand = new CarBrand(carBrandModel.getBrandName());
        this.carBrandRepository.save(carBrand);
        return new AccessTokenModel(carBrandModel.getAccessToken());
    }

    @PostMapping(path = "removeCarFromCompany")
    public AccessTokenModel removeCarFromCompany(@RequestBody AddCarToCompanyModel addCarToCompanyModel){
        Client client = clientRepository.findByToken(addCarToCompanyModel.getAccessToken());
        Car car = carService.getClientCar(client, addCarToCompanyModel.getCarId()).getCar();
        Company company = companyService.getClientCompany(client, addCarToCompanyModel.getCompanyId());
        CompaniesHasCars chc = companiesHasCarsRepository.getCompanyCarRelationship(company.getId(), car.getId());
        chc.setStatus(CompanyOwnershipStatus.FORMER_OWNER_COMPANY);
        companiesHasCarsRepository.saveCompaniesCarsRelationship(chc);

        return new AccessTokenModel(addCarToCompanyModel.getAccessToken());
    }


}
