package pl.warsztat.zlomek.model.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "companies_data")
public class CompanyData extends CompanyModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @OneToMany(mappedBy = "companyData")
    private Set<Invoice> invoices;

    public CompanyData(String nip, String companyName, String cityName, String streetName, String buildingNum, String aptNum, String zipCode, Set<Invoice> invoices) {
        super(nip, companyName, cityName, streetName, buildingNum, aptNum, zipCode);
        this.invoices = invoices;
    }

    public CompanyData(Company company){
        super(company.getNip(), company.getCompanyName(), company.getCityName(), company.getStreetName(),
                company.getBuildingNum(), company.getAptNum(), company.getZipCode());
        this.invoices = new HashSet<>();
    }

    public CompanyData(){
        this.invoices = new HashSet<>();
    }
}
