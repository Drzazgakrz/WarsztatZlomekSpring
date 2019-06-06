package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BanClientModel extends AcceptVisitModel {
    private String username;
}
