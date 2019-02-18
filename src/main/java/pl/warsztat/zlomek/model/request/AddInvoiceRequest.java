package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class AddInvoiceRequest extends AccessTokenModel {
    private int discount;
    private String methodOfPayment;
    private long visitId;
    private String companyName;
}
