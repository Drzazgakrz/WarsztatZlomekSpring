package pl.warsztat.zlomek.model.db;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
@lombok.AllArgsConstructor
@lombok.Setter
@lombok.Getter
@NoArgsConstructor
@Entity
@Table(name = "services")
public class Service implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "service_name")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "service")
    @NotNull
    private Set<VisitsHasServices> visits;

    private int tax;

    public Service(String name, int tax) {
        this.name = name;
        visits = new HashSet<>();
        this.tax = tax;
    }
}
