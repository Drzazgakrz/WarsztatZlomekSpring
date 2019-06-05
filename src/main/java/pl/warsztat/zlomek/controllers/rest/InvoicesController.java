package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.data.InvoicesRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Invoice;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;
import pl.warsztat.zlomek.model.request.AddInvoiceRequest;
import pl.warsztat.zlomek.model.request.EditInvoiceRequest;
import pl.warsztat.zlomek.model.response.InvoiceResponse;
import pl.warsztat.zlomek.model.response.ProFormaInvoiceResponse;
import pl.warsztat.zlomek.service.InvoiceService;

@RestController
@RequestMapping(path = "/rest/invoices")
public class InvoicesController {

    private InvoicesRepository invoicesRepository;
    private EmployeeRepository employeeRepository;
    private CompaniesRepository companiesRepository;
    private InvoiceService invoiceService;

    @Autowired
    public InvoicesController(InvoicesRepository invoicesRepository, EmployeeRepository employeeRepository,
                              CompaniesRepository companiesRepository, InvoiceService invoiceService){
        this.invoicesRepository = invoicesRepository;
        this.companiesRepository = companiesRepository;
        this.employeeRepository = employeeRepository;
        this.invoiceService = invoiceService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public InvoiceResponse addInvoice(@RequestBody AddInvoiceRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        Invoice invoice = this.invoiceService.createVatInvoice(request);
        return new InvoiceResponse(request.getAccessToken(), invoice);
    }

    @RequestMapping(method = RequestMethod.POST, path = "addProForma")
    public ProFormaInvoiceResponse addProFormaInvoice(@RequestBody AddInvoiceRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        InvoiceBuffer invoice = this.invoiceService.createProFormaInvoice(request);
        return new ProFormaInvoiceResponse(request.getAccessToken(), invoice);
    }

    @RequestMapping(method = RequestMethod.POST, path = "edit")
    public InvoiceResponse addInvoice(@RequestBody EditInvoiceRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        Invoice currentInvoice = this.invoicesRepository.getInvoiceById(request.getInvoiceId());
        Invoice invoice = this.invoiceService.createVatInvoice(request);
        currentInvoice.setCorectionInvoice(invoice);
        this.invoicesRepository.update(currentInvoice);
        return new InvoiceResponse(request.getAccessToken(), currentInvoice);
    }
}
