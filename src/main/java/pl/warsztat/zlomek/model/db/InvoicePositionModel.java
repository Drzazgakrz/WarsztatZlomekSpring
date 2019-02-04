package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public class InvoicePositionModel {
    @NotNull
    @Size(min = 2,max = 45)
    @Column(name = "position_name")
    protected String itemName;

    @NotNull
    @Size(min=1, max=5)
    @Column(name="unit_of_measure")
    protected String unitOfMeasure;

    @NotNull
    @Column(name="gross_price", precision=20 , scale =2 )
    protected BigDecimal grossPrice;

    @NotNull
    @Column(name="net_price", precision=20 , scale =2 )
    protected BigDecimal netPrice;

    @NotNull
    @Column(name = "VAT_tax")
    protected int vat;

    @NotNull
    @Column(name = "value_of_VAT", precision=20 , scale =2 )
    protected BigDecimal valueOfVat;

    @NotNull
    protected int count;
}
