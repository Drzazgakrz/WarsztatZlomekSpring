package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarBrandsList {
    private List<CarBrandResponse> brands;

    public CarBrandsList(List<CarBrandResponse> brands){
        this.brands = brands;
    }
}
