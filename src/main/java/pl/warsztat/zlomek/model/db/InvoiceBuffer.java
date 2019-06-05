package pl.warsztat.zlomek.model.db;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "invoices_buffer")
public class InvoiceBuffer extends InvoicesModel implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;

    @Column(name = "ststus")
    private InvoiceBufferStatus invoiceBufferStatus;

    @OneToMany(mappedBy = "invoiceBuffer")
    private Set<InvoiceBufferPosition> invoiceBufferPositions;

    @OneToOne
    private Visit visit;

    public InvoiceBuffer(int discount, MethodOfPayment methodOfPayment, CompanyData companyData,
                         CarServiceData carServiceData, LocalDate paymentDate, Visit visit, String invoiceNumber){
        super(discount, methodOfPayment, carServiceData, LocalDate.now(), paymentDate, companyData, invoiceNumber);
        invoiceBufferPositions = new HashSet<>();
        this.carServiceData = carServiceData;
        this.visit = visit;
        this.companyData = companyData;
    }

    public InvoiceBuffer() {
        this.dayOfIssue = LocalDate.now();
        this.grossValue = new BigDecimal(0);
        this.netValue = new BigDecimal(0);
        this.invoiceBufferPositions = new HashSet<>();
    }

    public void add(InvoiceBufferPosition invoiceBufferPosition){
        this.invoiceBufferPositions.add(invoiceBufferPosition);
    }
}
