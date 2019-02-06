package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.Visit;

import java.sql.Date;
import java.time.ZoneId;

@Getter
public class AddVisitModel extends VisitModel {
    private String accessToken;
}
