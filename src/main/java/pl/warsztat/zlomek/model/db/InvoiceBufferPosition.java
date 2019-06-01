package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "invoice_buffer_position")
@Getter
@Setter
@NoArgsConstructor
public class InvoiceBufferPosition extends InvoicePositionModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "invoice_buffer_id")
    private InvoiceBuffer invoiceBuffer;

    public InvoiceBufferPosition(VisitPosition position, String unitOfMeasure, String itemName, int tax, @NotNull InvoiceBuffer invoiceBuffer) {
        super(position, unitOfMeasure, itemName, tax);
        this.invoiceBuffer = invoiceBuffer;
    }
}
