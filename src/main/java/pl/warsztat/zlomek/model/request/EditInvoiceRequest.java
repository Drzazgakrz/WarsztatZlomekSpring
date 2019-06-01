package pl.warsztat.zlomek.model.request;

import lombok.Getter;

@Getter
public class EditInvoiceRequest extends AddInvoiceRequest {
    private long invoiceId;
}
