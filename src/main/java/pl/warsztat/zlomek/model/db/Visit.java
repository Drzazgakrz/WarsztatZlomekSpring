package pl.warsztat.zlomek.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @NotNull
    private VisitStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id")
    @JsonIgnore
    private Overview overview;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "visit")
    @NotNull
    @JsonIgnore
    private Set<VisitsHasServices> services;

    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<VisitsParts> parts;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnore
    private Client client;

    @Column(name = "visit_finished")
    private LocalDate visitFinished;

    public Visit(Date visitDate, Car car, Client client, Overview overview){
        this.visitDate = visitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.car = car;
        this.client = client;
        this.services = new HashSet<>();
        this.parts = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.overview = overview;
        this.status = VisitStatus.NEW;
    }

    public void addService(VisitsHasServices vhs){
        this.services.add(vhs);
    }

    public void addCarPart(VisitsParts vp){
        this.parts.add(vp);
    }

    @Override
    public String toString() {
        return  "Pracownik=" + employee.getFirstName() + " "+employee.getLastName() +
                ", samochód=" + car.getBrand().getBrandName() + " " + car.getModel() +
                ", zakończona=" + visitFinished;
    }
}
