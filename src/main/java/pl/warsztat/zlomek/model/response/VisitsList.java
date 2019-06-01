package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

import java.util.List;

@Getter
public class VisitsList extends AccessTokenModel {
    private List<VisitResponse> visits;

    public VisitsList(String accessToken, List<VisitResponse> visits) {
        super(accessToken);
        this.visits = visits;
    }
}
