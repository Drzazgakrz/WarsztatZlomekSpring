package pl.warsztat.zlomek.model.request;

import lombok.Getter;

@Getter
public class AddElementToVisitModel {
    private long elementId;
    private String price;
    private int count;
}
