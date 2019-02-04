package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;
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
    private Employee employee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id")
    private Overview overview;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "visit")
    @NotNull
    private Set<VisitsHasServices> services;

    @OneToMany(mappedBy = "visit", fetch = FetchType.LAZY)
    private Set<VisitsParts> parts;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "visit_finished")
    private LocalDate visitFinished;

}
