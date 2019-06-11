package pl.warsztat.zlomek.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends CompanyModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(max = 30, min = 6)
    @Pattern(regexp = "[A-Za-z0-9._-]{1,}+@+[a-z0-9]{1,6}+.+[a-z]{2,3}")
    @Column(unique = true)
    private String email;


    @OneToMany(mappedBy = "company")
    private Set<CompaniesHasEmployees> employees;

    @OneToMany(mappedBy = "company")
    @JsonIgnore
    private Set<CompaniesHasCars> cars;

    public Company(String nip, String email, String companyName, String cityName, String streetName, String buildingNum, String aptNum, String zipCode){
        super(nip, companyName, cityName, streetName, buildingNum, aptNum, zipCode);
        this.email = email;
        this.employees = new HashSet<>();
        this.cars = new HashSet<>();
    }


    public CompaniesHasCars addCar(Car car){
        CompaniesHasCars companiesHasCars = new CompaniesHasCars(car, this);
        this.cars.add(companiesHasCars);
        car.addCompany(companiesHasCars);
        return companiesHasCars;
    }

    public CompaniesHasEmployees addClientToCompany(Client client){
        CompaniesHasEmployees companiesHasEmployees = new CompaniesHasEmployees(client, this);
        this.getEmployees().add(companiesHasEmployees);
        return companiesHasEmployees;
    }

    public Company(){
        this.cars = new HashSet<>();
        this.employees = new HashSet<>();
    }

    @Override
    public String toString() {
        return  "nip=" + nip + ' ' +
                "nazwa=" + companyName;
    }

    public void copy(Company company){
        this.email = company.getEmail();
        this.aptNum = company.getAptNum();
        this.companyName = company.getCompanyName();
        this.nip = company.getNip();
        this.buildingNum = company.getBuildingNum();
        this.cityName = company.getCityName();
        this.streetName = company.getStreetName();
        this.zipCode = company.getZipCode();
    }
}
