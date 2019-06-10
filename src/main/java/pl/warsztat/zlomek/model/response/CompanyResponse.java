package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.CompanyModel;

@Getter
public class CompanyResponse {
    private long id;
    private String nip;
    private String companyName;
    private String cityName;
    private String streetName;
    private String buildingNum;
    private String aptNum;
    private String zipCode;
    private String email;

    public CompanyResponse(CompanyModel companyModel){
        this.id = companyModel.getId();
        this.aptNum = companyModel.getAptNum();
        this.buildingNum = companyModel.getBuildingNum();
        this.cityName = companyModel.getCityName();
        this.companyName = companyModel.getCompanyName();
        this.nip = companyModel.getNip();
        this.streetName = companyModel.getStreetName();
        this.zipCode = companyModel.getZipCode();
        this.email = companyModel.getEmail();
    }
}
