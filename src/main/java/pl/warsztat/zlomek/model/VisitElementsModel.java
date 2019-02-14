package pl.warsztat.zlomek.model;

import lombok.Getter;

@Getter
public class VisitElementsModel {
    private long id;
    private String name;
    private int tax;

    public VisitElementsModel(long id, String name, int tax) {
        this.id = id;
        this.name = name;
        this.tax = tax;
    }
}
