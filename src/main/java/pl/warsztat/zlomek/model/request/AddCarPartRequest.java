package pl.warsztat.zlomek.model.request;

import lombok.Getter;

@Getter
public class AddCarPartRequest extends AddVisitElementRequest {
    protected String producer;
}
