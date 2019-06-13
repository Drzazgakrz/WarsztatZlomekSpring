package pl.warsztat.zlomek.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddElementToVisitModel {
    private long id;
    private String price;
    private int count;
}
