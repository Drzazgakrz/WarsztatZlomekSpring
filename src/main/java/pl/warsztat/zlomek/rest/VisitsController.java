package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CarRepository;
import pl.warsztat.zlomek.data.ClientRepository;
import pl.warsztat.zlomek.data.OverviewRepository;
import pl.warsztat.zlomek.data.VisitRepository;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.model.response.VisitResponse;
import pl.warsztat.zlomek.model.response.VisitsList;
import pl.warsztat.zlomek.service.CarService;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Car;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.Overview;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.request.AddVisitModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/visits")
public class VisitsController {

    private CarRepository carRepository;
    private ClientRepository clientRepository;
    private OverviewRepository overviewRepository;
    private VisitRepository visitRepository;
    private CarService carService;

    @Autowired
    public VisitsController(ClientRepository clientRepository, CarRepository carRepository,
                            OverviewRepository overviewRepository, VisitRepository visitRepository, CarService carService){
        this.carRepository = carRepository;
        this.clientRepository = clientRepository;
        this.overviewRepository = overviewRepository;
        this.visitRepository = visitRepository;
        this.carService = carService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel createVisit(@RequestBody AddVisitModel visitModel){
        Client client = clientRepository.findByToken(visitModel.getAccessToken());
        Car car = carService.getClientCar(client, visitModel.getCarId());
        Overview overview = null;
        if(visitModel.isOverview()) {
            overview = new Overview(visitModel.getVisitDate(), car);
            overviewRepository.saveOverview(overview);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, 3);
        if(visitModel.getVisitDate().before(new Date()) || visitModel.getVisitDate().after(calendar.getTime()))
            throw new FieldsNotCorrect(new String[]{"visitDate"});
        Visit visit = new Visit(visitModel.getVisitDate(), car, client, overview);
        visitRepository.saveVisit(visit);
        car.addVisit(visit);
        carRepository.updateCar(car);
        return new AccessTokenModel(visitModel.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public VisitResponse getVisitData(@PathVariable long id, @RequestHeader("accessToken") String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        Visit visit = client.getVisits().stream().filter((currentVisit)-> currentVisit.getId() == id).findFirst().get();
        return new VisitResponse(visit, client);
    }

    @RequestMapping(method = RequestMethod.GET)
    public VisitsList getPreviousVisits(@RequestHeader("accessToken") String accessToken){
        Client client = clientRepository.findByToken(accessToken);
        List<VisitResponse> visits = new ArrayList<>();
        client.getVisits().forEach(visit->visits.add(new VisitResponse(visit,client)));
        return new VisitsList(accessToken, visits);
    }

    //@RequestMapping(method = RequestMethod.GET)
}
