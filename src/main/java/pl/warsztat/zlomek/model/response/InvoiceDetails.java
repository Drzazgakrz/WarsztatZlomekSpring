package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Getter
public class InvoiceDetails {
    private InvoicePositionResponse[] invoicePositions;
    private InvoiceDetails corectionInvoice;
    private Date visitFinished;
    private String methodOfPayment;
    private String invoiceNumber;
    private CompanyResponse carServiceData;
    private Date dayOfIssue;
    private Date paymentDate;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private CompanyResponse companyData;

    public InvoiceDetails(Invoice invoice){
        this.methodOfPayment = invoice.getMethodOfPayment().toString();
        this.invoiceNumber = invoice.getInvoiceNumber();
        this.carServiceData = new CompanyResponse(invoice.getCarServiceData());
        this.dayOfIssue = Date.from(invoice.getDayOfIssue().atStartOfDay().atZone(ZoneId.systemDefault()).
                toInstant());
        this.paymentDate = Date.from(invoice.getPaymentDate().atStartOfDay().atZone(ZoneId.systemDefault()).
                toInstant());
        this.netValue = invoice.getNetValue();
        this.grossValue = invoice.getGrossValue();
        this.companyData = new CompanyResponse(invoice.getCompanyData());
        if(invoice.getCorectionInvoice() != null){
            this.corectionInvoice = new InvoiceDetails(invoice.getCorectionInvoice());
        }
        this.visitFinished = Date.from(invoice.getVisitFinished().atStartOfDay()
                .atZone(ZoneId.systemDefault()).toInstant());
        int i = 0;
        Set<InvoicePosition> positions = invoice.getInvoicePositions();
        this.invoicePositions = new InvoicePositionResponse[positions.size()];
        for (InvoicePositionModel position: positions){
            this.invoicePositions[i] = new InvoicePositionResponse(position);
            i++;
        }
    }
}
