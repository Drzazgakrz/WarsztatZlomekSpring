package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class EditVisitRequest extends AccessTokenModel {
    private long visitId;
    private String status;
    private int countYears;
    private AddElementToVisitModel[] services;
    private AddElementToVisitModel[] carParts;
}
