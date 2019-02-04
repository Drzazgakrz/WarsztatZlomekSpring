package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import pl.warsztat.zlomek.model.db.Car;

@Getter
@Setter
public class CarData {
    protected String carBrandName;
    protected String model;
    protected int prodYear;
    protected String vinNumber;
    protected String accessToken;
    protected String registrationNumber;
}
