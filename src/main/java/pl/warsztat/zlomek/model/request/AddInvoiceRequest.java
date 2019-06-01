package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
@Setter
@NoArgsConstructor
public class AddInvoiceRequest extends AccessTokenModel {
    private int discount;
    private String methodOfPayment;
    private long visitId;
    private String companyName;
}
