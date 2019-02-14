package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CarPartsRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.data.ServicesRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.VisitElementsModel;
import pl.warsztat.zlomek.model.db.CarPart;
import pl.warsztat.zlomek.model.db.Service;
import pl.warsztat.zlomek.model.request.AddCarPartRequest;
import pl.warsztat.zlomek.model.request.AddVisitElementRequest;
import pl.warsztat.zlomek.model.request.EditCarPart;
import pl.warsztat.zlomek.model.request.EditService;
import pl.warsztat.zlomek.model.response.VisitElementsList;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/visitElements")
public class VisitElementsController {

    private EmployeeRepository employeeRepository;
    private CarPartsRepository carPartsRepository;
    private ServicesRepository servicesRepository;
    @Autowired
    public VisitElementsController(EmployeeRepository employeeRepository, CarPartsRepository carPartsRepository,
                                   ServicesRepository servicesRepository){
        this.employeeRepository = employeeRepository;
        this.carPartsRepository = carPartsRepository;
        this.servicesRepository = servicesRepository;
    }
    @RequestMapping(method = RequestMethod.GET)
    public VisitElementsList getVisitElements(@RequestHeader("accessToken") AccessTokenModel accessToken){
        this.employeeRepository.findByToken(accessToken.getAccessToken());
        List<VisitElementsModel> carParts = new ArrayList<>();
        this.carPartsRepository.getCarParts().forEach(visitElement->{
            carParts.add(new VisitElementsModel(visitElement.getId(), visitElement.getName()+", "
                    +visitElement.getProducer(), visitElement.getTax()));
        });

        List<VisitElementsModel> services = new ArrayList<>();
        this.servicesRepository.getAllServices().forEach((service -> {
            services.add(new VisitElementsModel(service.getId(), service.getName(), service.getTax()));
        }));
        return new VisitElementsList(accessToken.getAccessToken(), carParts, services);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/service")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addService(@RequestBody AddVisitElementRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        Service service = new Service(request.getName(), request.getTax());
        this.servicesRepository.saveService(service);
        return new AccessTokenModel(request.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/carPart")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addCarPart(@RequestBody AddCarPartRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        CarPart carPart = new CarPart(request.getName(), request.getTax(), request.getProducer());
        this.carPartsRepository.saveCarPart(carPart);
        return new AccessTokenModel(request.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "service")
    public AccessTokenModel editService(@RequestBody EditService request){
        this.employeeRepository.findByToken(request.getAccessToken());
        Service service = this.servicesRepository.getServiceById(request.getId());
        service.setName(request.getName());
        service.setTax(request.getTax());
        this.servicesRepository.updateService(service);
        return new AccessTokenModel(request.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.PUT, path = "carPart")
    public AccessTokenModel editCarPart(@RequestBody EditCarPart request){
        this.employeeRepository.findByToken(request.getAccessToken());
        CarPart carPart = this.carPartsRepository.getCarPartById(request.getId());
        carPart.setTax(request.getTax());
        carPart.setName(request.getName());
        carPart.setProducer(request.getProducer());
        this.carPartsRepository.updateCarPart(carPart);
        return new AccessTokenModel(request.getAccessToken());
    }
}
