package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class VisitPosition {

    @Column(name = "single_price")
    protected BigDecimal singlePrice;

    protected int count;

    public VisitPosition(BigDecimal singlePartPrice, int count) {
        this.singlePrice = singlePartPrice;
        this.count = count;
    }
}
