package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Invoice;

@Getter
public class InvoiceResponse extends AccessTokenModel {
    private InvoiceDetails invoice;

    public InvoiceResponse(String accessToken, Invoice invoice) {
        super(accessToken);
        this.invoice = new InvoiceDetails(invoice);
    }
}
