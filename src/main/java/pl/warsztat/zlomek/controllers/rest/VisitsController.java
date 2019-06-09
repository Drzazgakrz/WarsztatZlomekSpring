package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.AcceptVisitModel;
import pl.warsztat.zlomek.model.request.AddElementToVisitModel;
import pl.warsztat.zlomek.model.request.EditVisitRequest;
import pl.warsztat.zlomek.model.response.VisitResponse;
import pl.warsztat.zlomek.model.response.VisitsList;
import pl.warsztat.zlomek.service.CarService;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.request.AddVisitModel;
import pl.warsztat.zlomek.service.OverviewsService;
import pl.warsztat.zlomek.service.VisitService;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/rest/visits")
public class VisitsController {

    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private OverviewRepository overviewRepository;
    private VisitRepository visitRepository;
    private CarService carService;
    private EmployeeRepository employeeRepository;
    private VisitService visitService;
    private OverviewsService overviewsService;

    @Autowired
    public VisitsController(ClientRepository clientRepository, CarRepository carRepository,
                            OverviewRepository overviewRepository, VisitRepository visitRepository, CarService carService,
                            EmployeeRepository employeeRepository, VisitService visitService,
                            OverviewsService overviewsService){
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.overviewRepository = overviewRepository;
        this.visitRepository = visitRepository;
        this.carService = carService;
        this.employeeRepository = employeeRepository;
        this.visitService = visitService;
        this.overviewsService = overviewsService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel createVisit(@RequestBody AddVisitModel visitModel){
        Client client = this.clientRepository.findByToken(visitModel.getAccessToken());
        Car car = this.carService.getClientCar(client, visitModel.getCarId()).getCar();
        Overview overview = null;
        if(visitModel.isOverview()) {
            overview = new Overview(visitModel.getVisitDate(), car);
            this.overviewRepository.saveOverview(overview);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 3);
        if(visitModel.getVisitDate().before(new Date()) || visitModel.getVisitDate().after(calendar.getTime()))
            throw new FieldsNotCorrect(new String[]{"visitDate"}, visitModel.getAccessToken());
        Visit visit = new Visit(visitModel.getVisitDate(), car, client, overview);
        this.visitRepository.saveVisit(visit);
        car.addVisit(visit);
        this.carRepository.updateCar(car);
        return new AccessTokenModel(visitModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "createEmpty")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel createEmptyVisit(@RequestBody AddVisitModel visitModel){
        Employee employee = this.employeeRepository.findByToken(visitModel.getAccessToken());
        Client client = this.clientRepository.getClientById(1);
        Car car = this.carRepository.getCarById(1);
        Overview overview = null;
        if(visitModel.isOverview()) {
            overview = new Overview(visitModel.getVisitDate(), car);
            this.overviewRepository.saveOverview(overview);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 3);
        if(visitModel.getVisitDate().before(new Date()) || visitModel.getVisitDate().after(calendar.getTime()))
            throw new FieldsNotCorrect(new String[]{"visitDate"}, visitModel.getAccessToken());
        Visit visit = new Visit(visitModel.getVisitDate(), car, client, overview);
        visit.setEmployee(employee);
        visit.setStatus(VisitStatus.ACCEPTED);
        employee.addVisit(visit);
        this.visitRepository.saveVisit(visit);
        car.addVisit(visit);
        this.carRepository.updateCar(car);
        return new AccessTokenModel(visitModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/{id}")
    public VisitResponse getVisitData(@PathVariable long id, @RequestBody AccessTokenModel accessToken){
        Client client = this.clientRepository.findByToken(accessToken.getAccessToken());
        Visit visit = client.getVisits().stream().filter((currentVisit)-> currentVisit.getId() == id).findFirst().get();
        return new VisitResponse(visit, client);
    }
    @RequestMapping(method = RequestMethod.POST, path = "employeeVisit/{id}")
    public VisitResponse getVisitDataEmployee(@PathVariable long id, @RequestBody AccessTokenModel accessToken){
        Employee employee = this.employeeRepository.findByToken(accessToken.getAccessToken());
        Visit visit = employee.getVisits().stream().filter((currentVisit)-> currentVisit.getId() == id).findFirst().get();
        return new VisitResponse(visit);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/previous")
    public VisitsList getPreviousVisits(@RequestBody AccessTokenModel accessToken){
        Client client = this.clientRepository.findByToken(accessToken.getAccessToken());
        List<VisitResponse> visits = new ArrayList<>();
        List<Visit> previousVisits = client.getVisits().stream().filter((visit ->
                visit.getVisitDate().isBefore(LocalDateTime.now()))).collect(Collectors.toList());
        previousVisits.forEach(visit->visits.add(new VisitResponse(visit,client)));
        return new VisitsList(accessToken.getAccessToken(), visits);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/futureVisits")
    public VisitsList getFutureVisits(@RequestBody AccessTokenModel accessToken){
        Client client = this.clientRepository.findByToken(accessToken.getAccessToken());
        List<VisitResponse> visits = new ArrayList<>();
        List<Visit> futureVisits = client.getVisits().stream().filter(visit ->
                visit.getVisitDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        futureVisits.forEach(visit->visits.add(new VisitResponse(visit,client)));
        return new VisitsList(accessToken.getAccessToken(), visits);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/acceptedVisit")
    public AccessTokenModel acceptVisit(@RequestBody AcceptVisitModel acceptVisitModel){
        Employee employee = this.employeeRepository.findByToken(acceptVisitModel.getAccessToken());
        Visit visit = this.visitRepository.getVisitById(acceptVisitModel.getVisitId(), VisitStatus.NEW);
        visit.setEmployee(employee);
        employee.addVisit(visit);
        visit.setStatus(VisitStatus.ACCEPTED);
        this.visitRepository.updateVisit(visit);
        return new AccessTokenModel(acceptVisitModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/edit")
    public AccessTokenModel editVisit(@RequestBody EditVisitRequest editVisitRequest){
        this.employeeRepository.findByToken(editVisitRequest.getAccessToken());
        VisitStatus status = this.visitService.getStatus(editVisitRequest.getStatus());
        Visit visit = this.visitRepository.getVisitById(editVisitRequest.getVisitId());
        visit.setStatus(status);
        AddElementToVisitModel[] services = editVisitRequest.getServices();
        if(services != null && services.length > 0)
            this.visitService.addServicesToVisit(visit, editVisitRequest.getServices());
        if(editVisitRequest.getCarParts() != null)
            this.visitService.addCarPartsToVisit(visit, editVisitRequest.getServices());
        this.visitRepository.updateVisit(visit);
        Overview overview = visit.getOverview();
        visit.setVisitFinished(LocalDate.now());
        this.overviewsService.setOverviewDate(overview, editVisitRequest.getCountYears(), visit);
        return new AccessTokenModel(editVisitRequest.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/employee/{status}")
    public VisitsList getVisitsByStatus(@RequestBody AccessTokenModel accessToken, @PathVariable(name = "status") String status){
        employeeRepository.findByToken(accessToken.getAccessToken());
        List<Visit> visits = visitRepository.getVisitByStatus(visitService.getStatus(status));
        ArrayList<VisitResponse> visitResponses = new ArrayList<>();
        visits.forEach(visit -> visitResponses.add(new VisitResponse(visit)));
        return new VisitsList(accessToken.getAccessToken(), visitResponses);
    }

    @PostMapping(path = "remove/{id}")
    public AccessTokenModel removeVisit(@RequestBody AccessTokenModel accessToken, @PathVariable long id){
        Client client = clientRepository.findByToken(accessToken.getAccessToken());
        Visit visit = client.getVisits().stream().filter(current -> current.getId() == id).findFirst().or(() -> {
            throw new ResourcesNotFoundException("Klient nie ma takiej wizyty");
        }).get();
        if(!visit.getStatus().equals(VisitStatus.NEW))
            throw new ResourcesNotFoundException("Wizyta została zaakceptowana");
        this.visitRepository.remove(visit);
        return accessToken;
    }

    @PostMapping(path = "all")
    public VisitsList getAllLists(@RequestBody AccessTokenModel accessToken){
        Client client = this.clientRepository.findByToken(accessToken.getAccessToken());
        List<VisitResponse> visits = this.visitService.getAllClientsVisits(client);
        return new VisitsList(accessToken.getAccessToken(), visits);
    }
}
