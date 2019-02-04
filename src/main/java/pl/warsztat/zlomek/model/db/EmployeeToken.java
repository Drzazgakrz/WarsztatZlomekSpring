package pl.warsztat.zlomek.model.db;

import javax.persistence.*;

@Entity
@Table(name = "employee_token")
public class EmployeeToken extends AccessToken{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
