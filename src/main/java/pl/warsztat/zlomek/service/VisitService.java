package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.AddElementToVisitModel;

import java.math.BigDecimal;
import java.util.Arrays;
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
                        VisitsHasPartsRepository visitsHasPartsRepository){
        this.carPartsRepository = carPartsRepository;
        this.servicesRepository = servicesRepository;
        this.visitRepository = visitRepository;
        this.visitsHasServicesRepository = visitsHasServicesRepository;
        this.visitsHasPartsRepository = visitsHasPartsRepository;
    }
    public VisitStatus getStatus(String status){
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

    public void addServicesToVisit(Visit visit, AddElementToVisitModel[] model){
        Stream<AddElementToVisitModel> services = Arrays.stream(model);
        services.forEach(serviceModel->{
            pl.warsztat.zlomek.model.db.Service service = this.servicesRepository.getServiceById(serviceModel.getElementId());
            VisitsHasServices vhs = new VisitsHasServices(service, visit, serviceModel.getCount(),
                    new BigDecimal(serviceModel.getPrice()));
            visit.addService(vhs);
            service.addVisit(vhs);
            this.visitRepository.updateVisit(visit);
            this.visitsHasServicesRepository.save(vhs);
        });
    }

    public void addCarPartsToVisit(Visit visit, AddElementToVisitModel[] model){
        Stream<AddElementToVisitModel> carParts = Arrays.stream(model);
        carParts.forEach(carPartModel->{
            CarPart carPart = this.carPartsRepository.getCarPartById(carPartModel.getElementId());
            VisitsParts vp = new VisitsParts(visit, carPart, carPartModel.getCount(),
                    new BigDecimal(carPartModel.getPrice()));
            visit.addCarPart(vp);
            carPart.addVisit(vp);
            this.visitRepository.updateVisit(visit);
            this.visitsHasPartsRepository.persist(vp);
        });
    }
}
