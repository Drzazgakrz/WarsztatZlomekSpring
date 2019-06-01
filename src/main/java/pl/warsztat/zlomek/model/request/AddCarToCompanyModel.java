package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class AddCarToCompanyModel extends AccessTokenModel {
    private long carId;
    private long companyId;
}
