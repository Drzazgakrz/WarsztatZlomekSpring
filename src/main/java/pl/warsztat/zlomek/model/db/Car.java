package pl.warsztat.zlomek.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import pl.warsztat.zlomek.model.request.CarData;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

@Entity
@Table(name = "cars")
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[A-Z]{1}+[A-Za-z0-9/]{1,}")
    private String model;

    @NotNull
    @Min(value = 1930)
    @Column(name = "prod_year")
    private int prodYear;

    @NotNull
    @Size(min = 17, max = 17)
    @Column(name = "vin_number", unique = true)
    private String vin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id")
    @NotNull
    private CarBrand brand;

    @OneToMany(fetch = FetchType.LAZY ,mappedBy = "car")
    private Set<CarsHasOwners> owners;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "car")
    private Set<CompaniesHasCars> companiesCars;

    @OneToMany(mappedBy = "car")
    private Set<Visit> visits;

    @OneToMany(mappedBy = "car")
    private Set<Overview> overviews;

    public Car(String vin, String model, int prodYear, CarBrand brand){
        this.brand = brand;
        this.model = model;
        this.prodYear = prodYear;
        this.owners = new HashSet<>();
        this.vin = vin;
        this.companiesCars = new HashSet<>();
        this.overviews = new HashSet<>();
    }

    public CarsHasOwners addCarOwner(Client client, OwnershipStatus status, String registrationNumber){
        CarsHasOwners cho = new CarsHasOwners(this, client, status, registrationNumber);
        this.owners.add(cho);
        client.getCars().add(cho);
        return cho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return Objects.equals(vin, car.vin);

    }
    public void addVisit(Visit visit){
        this.visits.add(visit);
    }

    public void addCompany(CompaniesHasCars chc){
        this.companiesCars.add(chc);
    }

    public long getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public int getProdYear() {
        return prodYear;
    }

    public String getVin() {
        return vin;
    }

    public CarBrand getBrand() {
        return brand;
    }

    public Set<CarsHasOwners> getOwners() {
        return owners;
    }

    @JsonIgnore
    public Set<CompaniesHasCars> getCompaniesCars() {
        return companiesCars;
    }

    public Set<Visit> getVisits() {
        return visits;
    }

    public Set<Overview> getOverviews() {
        return overviews;
    }

    public void edit(CarData carData, CarBrand carBrand){
        if(!carData.getModel().equals(""))
            this.model = carData.getModel();
        if(carData.getProductionYear()>1930)
            this.prodYear = carData.getProductionYear();
        if(carBrand!=null)
            this.brand = carBrand;

    }
}
