package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Invoice;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;

@Getter
public class ProFormaInvoiceResponse extends AccessTokenModel {
    private ProFormaInvoiceDetails invoice;

    public ProFormaInvoiceResponse(String accessToken, InvoiceBuffer invoice) {
        super(accessToken);
        this.invoice = new ProFormaInvoiceDetails(invoice);
    }
}

