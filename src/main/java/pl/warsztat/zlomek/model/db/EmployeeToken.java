package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_token")
@Getter
@Setter
@NoArgsConstructor
public class EmployeeToken extends AccessToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public EmployeeToken(String accessToken, LocalDateTime expiration, Employee employee) {
        super(accessToken, expiration);
        this.employee = employee;
    }
}
