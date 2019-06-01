package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.InvoicePositionModel;

import java.math.BigDecimal;

@Getter
public class InvoicePositionResponse {
    protected String itemName;
    protected String unitOfMeasure;
    protected BigDecimal grossPrice;
    protected BigDecimal netPrice;
    protected int vat;
    protected BigDecimal valueOfVat;
    protected int count;

    public InvoicePositionResponse(InvoicePositionModel position) {
        this.itemName = position.getItemName();
        this.unitOfMeasure = position.getUnitOfMeasure();
        this.grossPrice = position.getGrossPrice();
        this.netPrice = position.getNetPrice();
        this.vat = position.getVat();
        this.valueOfVat = position.getValueOfVat();
        this.count = position.getCount();
    }
}
