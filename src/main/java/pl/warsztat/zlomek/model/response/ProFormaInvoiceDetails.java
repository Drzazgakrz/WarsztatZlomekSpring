package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Getter
public class ProFormaInvoiceDetails {
    private InvoicePositionResponse[] invoicePositions;
    private String methodOfPayment;
    private String invoiceNumber;
    private CompanyResponse carServiceData;
    private Date dayOfIssue;
    private Date paymentDate;
    private BigDecimal netValue;
    private BigDecimal grossValue;
    private CompanyResponse companyData;

    public ProFormaInvoiceDetails(InvoiceBuffer invoice){
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
        int i = 0;
        Set<InvoiceBufferPosition> positions = invoice.getInvoiceBufferPositions();
        this.invoicePositions = new InvoicePositionResponse[positions.size()];
        for (InvoicePositionModel position: positions){
            this.invoicePositions[i] = new InvoicePositionResponse(position);
            i++;
        }
    }
}
