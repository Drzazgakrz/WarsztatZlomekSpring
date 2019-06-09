package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class InvoicesModel {

    protected int discount;

    @Column(name = "method_of_payment")
    protected MethodOfPayment methodOfPayment;


    @NotNull
    @Column(name = "invoice_number")
    protected String invoiceNumber;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "car_service_data_id")
    protected CarServiceData carServiceData;

    @NotNull
    @Column(name = "day_of_issue")
    protected LocalDate dayOfIssue;

    @NotNull
    @Column(name = "payment_date")
    protected LocalDate paymentDate;

    @Column(precision = 20, scale = 2, name = "net_value")
    protected BigDecimal netValue;

    @Column(precision = 20, scale = 2, name = "gross_value")
    protected BigDecimal grossValue;

    @ManyToOne
    @JoinColumn(name = "company_data_id")
    protected CompanyData companyData;

    public InvoicesModel(int discount, MethodOfPayment methodOfPayment, CarServiceData carServiceData,
                         LocalDate dayOfIssue, LocalDate paymentDate, CompanyData companyData, String invoiceNumber) {
        this.discount = discount;
        this.methodOfPayment = methodOfPayment;
        this.carServiceData = carServiceData;
        this.dayOfIssue = dayOfIssue;
        this.paymentDate = paymentDate;
        this.companyData = companyData;
        this.invoiceNumber = invoiceNumber;
        this.netValue = new BigDecimal(0);
        this.grossValue = new BigDecimal(0);
    }

    public InvoicesModel(InvoicesModel invoicesModel) {
        this.discount = invoicesModel.getDiscount();
        this.methodOfPayment = invoicesModel.getMethodOfPayment();
        this.carServiceData = invoicesModel.getCarServiceData();
        this.dayOfIssue = invoicesModel.getDayOfIssue();
        this.paymentDate = invoicesModel.getPaymentDate();
        this.companyData = invoicesModel.getCompanyData();
    }
}
