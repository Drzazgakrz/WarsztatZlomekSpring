package pl.warsztat.zlomek.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "visits_has_parts")
public class VisitsParts extends VisitPosition implements Serializable {
    @Id
    @EmbeddedId
    private VisitsPartsId id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "visit_id")
    @JsonIgnore
    private Visit visit;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "part_id")
    @JsonIgnore
    private CarPart part;

    @Embeddable
    @Getter
    @Setter
    private static class VisitsPartsId implements Serializable{
        @Column(name = "part_pk")
        private long partId;

        @Column(name = "visit_pk")
        private long visitId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            VisitsPartsId that = (VisitsPartsId) o;
            return partId == that.partId &&
                    visitId == that.visitId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(partId, visitId);
        }
        public VisitsPartsId(Visit visit, CarPart carPart){
            this.partId = carPart.getId();
            this.visitId = visit.getId();
        }
        public VisitsPartsId(){}
    }

    public VisitsParts(Visit visit, CarPart carPart, int count, BigDecimal singlePartPrice){
        super(singlePartPrice, count);
        this.id = new VisitsPartsId(visit, carPart);
        this.visit = visit;
        this.part = carPart;
    }
}
