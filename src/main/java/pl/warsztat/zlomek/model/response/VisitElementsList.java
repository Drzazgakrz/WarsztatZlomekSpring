package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.VisitElementsModel;

import java.util.List;

@Getter
public class VisitElementsList extends AccessTokenModel {
    private List<VisitElementsModel> carParts;
    private List<VisitElementsModel> services;

    public VisitElementsList(String accessToken, List<VisitElementsModel> carParts, List<VisitElementsModel> services) {
        super(accessToken);
        this.carParts = carParts;
        this.services = services;
    }
}
