package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    public Employee() {
        this.status = EmployeeStatus.CURRENT_EMPLOYER;
        this.accessToken = new HashSet<>();
        this.visits = new HashSet<>();
    }

    public void copy(Employee employee){
        this.hireDate = employee.getHireDate();
        this.quitDate = employee.getQuitDate();
        if(this.quitDate != null)
            this.status = EmployeeStatus.FORMER_EMPLOYER;
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.email = employee.getEmail();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = employee.getPassword();
        if(!(encoder.matches(employee.getPassword(), this.password))&& !newPassword.equals(""))
            password = encoder.encode(newPassword);
    }
}
