package pl.warsztat.zlomek.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Service extends VisitElement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "service")
    @NotNull
    @JsonIgnore
    private Set<VisitsHasServices> visits;

    public Service(String name, int tax) {
        this.name = name;
        visits = new HashSet<>();
        this.tax = tax;
    }

    public void addVisit(VisitsHasServices vhs){
        this.visits.add(vhs);
    }
}
