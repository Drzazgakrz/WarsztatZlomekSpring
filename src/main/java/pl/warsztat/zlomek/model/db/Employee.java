package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee extends Account implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "quit_date")
    private LocalDate quitDate;

    @NotNull
    private EmployeeStatus status;

    @OneToMany (mappedBy = "employee")
    private Set<Visit> visits;

    @OneToMany(mappedBy = "employee")
    private Set<EmployeeToken> accessToken;

    private AccountType role;

    public void addVisit(Visit visit){
        this.visits.add(visit);
    }
}
