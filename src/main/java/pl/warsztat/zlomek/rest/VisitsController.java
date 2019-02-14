package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
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
@RequestMapping(path = "/visits")
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

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public VisitResponse getVisitData(@PathVariable long id, @RequestHeader("accessToken") String accessToken){
        Client client = this.clientRepository.findByToken(accessToken);
        Visit visit = client.getVisits().stream().filter((currentVisit)-> currentVisit.getId() == id).findFirst().get();
        return new VisitResponse(visit, client);
    }

    @RequestMapping(method = RequestMethod.GET)
    public VisitsList getPreviousVisits(@RequestHeader("accessToken") String accessToken){
        Client client = this.clientRepository.findByToken(accessToken);
        List<VisitResponse> visits = new ArrayList<>();
        List<Visit> previousVisits = client.getVisits().stream().filter((visit ->
                visit.getVisitDate().isBefore(LocalDateTime.now()))).collect(Collectors.toList());
        previousVisits.forEach(visit->visits.add(new VisitResponse(visit,client)));
        return new VisitsList(accessToken, visits);
    }

    @RequestMapping(method = RequestMethod.GET, path = "futureVisits")
    public VisitsList getFutureVisits(@RequestHeader("accessToken") String accessToken){
        Client client = this.clientRepository.findByToken(accessToken);
        List<VisitResponse> visits = new ArrayList<>();
        List<Visit> futureVisits = client.getVisits().stream().filter(visit ->
                visit.getVisitDate().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        futureVisits.forEach(visit->visits.add(new VisitResponse(visit,client)));
        return new VisitsList(accessToken, visits);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "acceptVisit")
    public AccessTokenModel acceptVisit(@RequestBody AcceptVisitModel acceptVisitModel){
        Employee employee = this.employeeRepository.findByToken(acceptVisitModel.getAccessToken());
        Visit visit = this.visitRepository.getVisitById(acceptVisitModel.getVisitId(), VisitStatus.NEW);
        visit.setEmployee(employee);
        employee.addVisit(visit);
        visit.setStatus(VisitStatus.ACCEPTED);
        this.visitRepository.updateVisit(visit);
        return new AccessTokenModel(acceptVisitModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.PUT)
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

    @RequestMapping(method = RequestMethod.GET, path = "/employee")
    public VisitsList getVisitsByStatus(@RequestHeader String accessToken, @RequestParam(name = "status") String status){
        employeeRepository.findByToken(accessToken);
        List<Visit> visits = visitRepository.getVisitByStatus(visitService.getStatus(status));
        ArrayList<VisitResponse> visitResponses = new ArrayList<>();
        visits.forEach(visit -> visitResponses.add(new VisitResponse(visit)));
        return new VisitsList(accessToken, visitResponses);
    }
}
