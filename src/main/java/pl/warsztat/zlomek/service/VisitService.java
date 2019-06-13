package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.AddElementToVisitModel;
import pl.warsztat.zlomek.model.response.VisitResponse;
import pl.warsztat.zlomek.model.response.VisitsList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class VisitService {

    private VisitRepository visitRepository;
    private ServicesRepository servicesRepository;
    private CarPartsRepository carPartsRepository;
    private VisitsHasPartsRepository visitsHasPartsRepository;
    private VisitsHasServicesRepository visitsHasServicesRepository;

    @Autowired
    public VisitService(ServicesRepository servicesRepository, CarPartsRepository carPartsRepository,
                        VisitRepository visitRepository, VisitsHasServicesRepository visitsHasServicesRepository,
                        VisitsHasPartsRepository visitsHasPartsRepository) {
        this.carPartsRepository = carPartsRepository;
        this.servicesRepository = servicesRepository;
        this.visitRepository = visitRepository;
        this.visitsHasServicesRepository = visitsHasServicesRepository;
        this.visitsHasPartsRepository = visitsHasPartsRepository;
    }

    public VisitStatus getStatus(String status) {
        switch (status) {
            case ("new"):
                return VisitStatus.NEW;
            case ("accepted"):
                return VisitStatus.ACCEPTED;
            case ("in_progress"):
                return VisitStatus.IN_PROGRESS;
            case ("for_pickup"):
                return VisitStatus.FOR_PICKUP;
            case ("finished"):
                return VisitStatus.FINISHED;
            default:
                throw new FieldsNotCorrect(new String[]{"status"});
        }
    }

    public void addServicesToVisit(Visit visit, AddElementToVisitModel[] model) {
        Stream<AddElementToVisitModel> services = Arrays.stream(model);
        services.forEach(serviceModel -> {
            pl.warsztat.zlomek.model.db.Service service = this.servicesRepository.getServiceById(serviceModel.getId());
            VisitsHasServices vhs = new VisitsHasServices(service, visit, serviceModel.getCount(),
                    new BigDecimal(serviceModel.getPrice()));
            visit.addService(vhs);
            service.addVisit(vhs);
            try {
                this.visitsHasServicesRepository.save(vhs);
            } catch (Exception e) {
                VisitsHasServices visitsHasServices = visit.getServices().stream().filter(part ->
                        part.getService().equals(vhs.getService())).findAny().get();
                visitsHasServices.setCount(vhs.getCount());
                this.visitsHasServicesRepository.update(visitsHasServices);
            }
            this.visitRepository.updateVisit(visit);
        });
    }

    public void addCarPartsToVisit(Visit visit, AddElementToVisitModel[] model) {
        Stream<AddElementToVisitModel> carParts = Arrays.stream(model);
        carParts.forEach(carPartModel -> {
            CarPart carPart = this.carPartsRepository.getCarPartById(carPartModel.getId());
            VisitsParts vp = new VisitsParts(visit, carPart, carPartModel.getCount(),
                    new BigDecimal(carPartModel.getPrice()));
            visit.addCarPart(vp);
            carPart.addVisit(vp);
            try {
                this.visitsHasPartsRepository.persist(vp);
            } catch (Exception e) {
                VisitsParts visitsParts = visit.getParts().stream().filter(part ->
                        part.getPart().equals(vp.getPart())).findAny().get();
                visitsParts.setCount(carPartModel.getCount());
                this.visitsHasPartsRepository.update(visitsParts);
            }
            this.visitRepository.updateVisit(visit);
        });
    }

    public String getTitleByStatus(VisitStatus status) {
        switch (status) {
            case NEW:
                return "Nowe wizyty";
            case ACCEPTED:
                return "Wizyty zaakceptowane";
            case IN_PROGRESS:
                return "Wizyty w trakcie wykonania";
            case FOR_PICKUP:
                return "Wizyty ukończone, samochód do odbioru";
            case FINISHED:
                return "Wizyty ukończone";
            default:
                throw new FieldsNotCorrect(new String[]{"status"});
        }
    }

    public List<VisitResponse> getAllClientsVisits(Client client) {
        List<Visit> visits = this.visitRepository.getAllClientsVisits(client);
        List<VisitResponse> response = new ArrayList<>();
        visits.forEach(visit -> {
            VisitResponse visitResponse = new VisitResponse(visit);
            response.add(visitResponse);
        });
        return response;
    }
}
