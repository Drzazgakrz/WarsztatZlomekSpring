package pl.warsztat.zlomek.model.db;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@lombok.Getter
@lombok.Setter
@Entity
@Table(name = "invoices")
public class Invoice extends InvoicesModel implements Serializable {
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private long id;

    //@NotNull
    @OneToMany (mappedBy = "invoice")
    private Set<InvoicePosition> invoicePositions;

    @OneToOne
    private Invoice corectionInvoice;

    @Column(name = "visit_finished")
    private LocalDate visitFinished;


    public Invoice(int discount, MethodOfPayment methodOfPayment,CompanyData companyData, CarServiceData carServiceData,
                   LocalDate paymentDate, String invoiceNumber){
        super(discount, methodOfPayment, carServiceData, LocalDate.now(), paymentDate, companyData, invoiceNumber);
        this.corectionInvoice = null;
        this.carServiceData = carServiceData;
        this.invoicePositions = new HashSet<>();
        this.dayOfIssue = LocalDate.now();
        this.invoiceNumber = invoiceNumber;
    }

    public Invoice(InvoiceBuffer buffer, CarServiceData data){
        super(buffer.getDiscount(), buffer.getMethodOfPayment(), data, LocalDate.now(), buffer.getPaymentDate(),
                buffer.getCompanyData(), buffer.invoiceNumber);
        this.netValue = buffer.getNetValue();
        this.grossValue = buffer.getGrossValue();
        this.invoicePositions = new HashSet<>();
        this.visitFinished = buffer.getVisit().getVisitFinished();
    }

    public Invoice(InvoiceBuffer invoiceBuffer) {
        super(invoiceBuffer);
        Visit visit = invoiceBuffer.getVisit();
        this.visitFinished = visit.getVisitFinished();
        this.invoicePositions = new HashSet<>();
    }

    public void add(InvoicePosition position){
        this.invoicePositions.add(position);
    }

    public Invoice() {
        this.dayOfIssue = LocalDate.now();
        this.grossValue = new BigDecimal(0);
        this.netValue = new BigDecimal(0);
        this.invoicePositions = new HashSet<>();
    }
}
