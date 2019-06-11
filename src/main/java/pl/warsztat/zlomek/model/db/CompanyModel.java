package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class CompanyModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    @NotNull
    @Size(min = 13,max=13)
    @Column(name = "NIP", unique = true)
    @Pattern(regexp = "[0-9]{3}+-+[0-9]{3}+-+[0-9]{2}+-+[0-9]{2}")
    protected String nip;

    @NotNull
    @Size(min = 2, max = 40)
    @Column(name = "company_name")
    protected String companyName;

    @NotNull
    @Size(max = 20, min = 2)
    @Column(name = "city_name")
    @Pattern(regexp = "[A-ZŹĄĘÓŁŻ]{1}+[a-z,ąęółńćźśż]{2,}")
    protected String cityName;

    @NotNull
    @Size(max = 40, min = 3)
    @Column(name = "street_name")
    @Pattern(regexp = "[A-ZŹĄĘÓŁŻ]{1}+[a-z,ąęółńćśźż]{2,}")
    protected String streetName;

    @NotNull
    @Size(max = 5, min = 1)
    @Column(name = "building_number")
    protected String buildingNum;

    @Size(max = 5)
    @Column(name = "apartment_number")
    protected String aptNum;

    @NotNull
    @Size(max = 6, min = 6)
    @Column(name = "zip_code")
    @Pattern(regexp = "[0-9]{2}+-+[0-9]{3}")
    protected String zipCode;

    @Column(unique = true)
    @Pattern(regexp = "[A-Za-z0-9._-]{1,}+@+[a-z]{1,6}+.+[a-z]{2,3}")
    protected String email;

    public CompanyModel(String nip, String companyName, String cityName, String streetName, String buildingNum, String aptNum, String zipCode) {
        this.nip = nip;
        this.companyName = companyName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.buildingNum = buildingNum;
        this.aptNum = aptNum;
        this.zipCode = zipCode;
    }

    public boolean compareCompanies(CompanyModel model){
        boolean result = this.nip.equals(model.getNip())&&this.companyName.equals(model.getCompanyName());
        result = result && this.buildingNum.equals(model.getBuildingNum()) && this.cityName.equals(model.getCityName());
        if(this.aptNum != null)
            result = result && this.aptNum.equals(model.getAptNum());
        return result && this.streetName.equals(model.getStreetName()) && this.zipCode.equals(model.getZipCode());
    }
}
