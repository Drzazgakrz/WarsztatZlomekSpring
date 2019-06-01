package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@lombok.AllArgsConstructor
@NoArgsConstructor
@lombok.ToString
@Entity
@Table(name = "car_parts")
public class CarPart extends VisitElement implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "part")
    private Set<VisitsParts> visits;

    @NotNull
    @Pattern(regexp = "[A-ZŹĄĘÓŁŻŚ]{1}+[a-z,śąęółńćźż]{2,}")
    private String producer;

    public CarPart(String name, int tax, String producer){
        this.name = name;
        this.tax = tax;
        this.producer = producer;
    }

    public void addVisit(VisitsParts visit){
        this.visits.add(visit);
    }
}
