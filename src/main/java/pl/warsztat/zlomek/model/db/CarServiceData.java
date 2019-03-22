package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Getter
@Setter
@Entity
@Table(name = "car_service_data")
public class CarServiceData extends CompanyModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(max = 30, min = 6)
    @Pattern(regexp = "[A-Za-z0-9._-]{1,}+@+[a-z0-9]{1,6}+.+[a-z]{2,3}")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "carServiceData")
    private Set<Invoice> invoices;

    @OneToMany(mappedBy = "carServiceData")
    private Set<InvoiceBuffer> invoicesBuffer;

    public CarServiceData(String NIP, String email, String serviceName, String cityName, String streetName, String buildingNum, String aptNum, String zipCode){
        this.nip = NIP;
        this.email = email;
        this.companyName = serviceName;
        this.cityName = cityName;
        this.streetName = streetName;
        this.buildingNum = buildingNum;
        this.aptNum = aptNum;
        this.zipCode = zipCode;
    }

    public CarServiceData(){
        this.invoices = new HashSet<>();
        this.invoicesBuffer = new HashSet<>();
    }
}
