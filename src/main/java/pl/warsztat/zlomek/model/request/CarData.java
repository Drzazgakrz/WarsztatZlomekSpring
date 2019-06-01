package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;
import pl.warsztat.zlomek.model.db.Client;

@Getter
@Setter
@NoArgsConstructor
public class CarData extends Car {
    protected String accessToken;

    public CarData(pl.warsztat.zlomek.model.db.Car car, String accessToken, Client client) {
        super(car, client);
        this.accessToken = accessToken;
    }
}
