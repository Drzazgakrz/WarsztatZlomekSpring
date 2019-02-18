package pl.warsztat.zlomek.model.db;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@lombok.NoArgsConstructor
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_service_data_id")
    private CarServiceData carServiceData;

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

    public void add(InvoiceBufferPosition invoiceBufferPosition){
        this.invoiceBufferPositions.add(invoiceBufferPosition);
    }
}
