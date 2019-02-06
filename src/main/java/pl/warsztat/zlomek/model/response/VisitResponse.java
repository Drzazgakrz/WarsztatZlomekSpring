package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.*;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class VisitResponse {
    private long id;
    private pl.warsztat.zlomek.model.request.Car car;
    private Date visitDate;
    private String visitStatus;
    private List<ClientDataResponse> verifiedOwners;
    private List<ClientDataResponse> notVerifiedOwners;

    public VisitResponse(Visit visit, Client client){
        this.id = visit.getId();
        this.car = new pl.warsztat.zlomek.model.request.Car(visit.getCar(), client);
        this.visitDate = Date.from(visit.getVisitDate().atZone(ZoneId.systemDefault()).toInstant());
        this.visitStatus = visit.getStatus().toString();
        List<CarsHasOwners> carsHasOwners= visit.getCar().getOwners().stream().filter((cho)->
                cho.getStatus().equals(OwnershipStatus.COOWNER) || cho.getStatus().equals(OwnershipStatus.CURRENT_OWNER))
                .collect(Collectors.toList());
        verifiedOwners = new ArrayList<>();
        carsHasOwners.forEach((cho)-> verifiedOwners.add(new ClientDataResponse(cho.getOwner(),null)));
        List<CarsHasOwners> notVerifiedOwners= visit.getCar().getOwners().stream().filter((cho)->
                cho.getStatus().equals(OwnershipStatus.NOT_VERIFIED_OWNER))
                .collect(Collectors.toList());
        this.notVerifiedOwners = new ArrayList<>();
        notVerifiedOwners.forEach((cho)-> this.notVerifiedOwners.add(new ClientDataResponse(cho.getOwner(),null)));
    }
}
