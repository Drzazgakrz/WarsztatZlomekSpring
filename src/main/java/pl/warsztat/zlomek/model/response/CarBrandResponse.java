package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import lombok.Setter;
import pl.warsztat.zlomek.model.db.CarBrand;

@Getter
@Setter
public class CarBrandResponse {
    private long id;
    private String brandName;

    public CarBrandResponse(CarBrand brand){
        this.brandName = brand.getBrandName();
    }

}
