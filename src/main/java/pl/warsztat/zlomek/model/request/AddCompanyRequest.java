package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class AddCompanyRequest extends AccessTokenModel {
    protected String name;
    protected String nip;
    protected String email;
    protected String cityName;
    protected String streetName;
    protected String buildingNum;
    protected String aptNum;
    protected String zipCode;
}
