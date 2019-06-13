package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.data.InvoiceBufferRepository;
import pl.warsztat.zlomek.data.InvoicesRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Invoice;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;
import pl.warsztat.zlomek.model.request.AddInvoiceRequest;
import pl.warsztat.zlomek.model.request.EditInvoiceRequest;
import pl.warsztat.zlomek.model.response.InvoiceDetails;
import pl.warsztat.zlomek.model.response.InvoiceResponse;
import pl.warsztat.zlomek.model.response.ProFormaInvoiceResponse;
import pl.warsztat.zlomek.service.InvoiceService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/rest/invoices")
public class InvoicesController {

    private InvoicesRepository invoicesRepository;
    private EmployeeRepository employeeRepository;
    private CompaniesRepository companiesRepository;
    private InvoiceBufferRepository invoiceBufferRepository;
    private InvoiceService invoiceService;

    @Autowired
    public InvoicesController(InvoicesRepository invoicesRepository, EmployeeRepository employeeRepository,
                              CompaniesRepository companiesRepository, InvoiceService invoiceService,
                              InvoiceBufferRepository invoiceBufferRepository){
        this.invoicesRepository = invoicesRepository;
        this.companiesRepository = companiesRepository;
        this.employeeRepository = employeeRepository;
        this.invoiceService = invoiceService;
        this.invoiceBufferRepository = invoiceBufferRepository;
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

    @PostMapping(path = "all")
    public List<InvoiceDetails> getInvoices(@RequestBody AccessTokenModel accessTokenModel){
        this.employeeRepository.findByToken(accessTokenModel.getAccessToken());
        List<InvoiceDetails> response = new ArrayList<>();
        this.invoicesRepository.getInvoices().forEach(invoice -> {
            InvoiceDetails invoiceResponse = new InvoiceDetails(invoice);
            response.add(invoiceResponse);
        });
        return response;
    }

    @PostMapping(path = "proForma/all")
    public List<InvoiceDetails> getProFormaInvoices(@RequestBody AccessTokenModel accessTokenModel){
        this.employeeRepository.findByToken(accessTokenModel.getAccessToken());
        List<InvoiceDetails> response = new ArrayList<>();
        this.invoiceBufferRepository.getInvoices().forEach(invoice -> {
            InvoiceDetails invoiceResponse = new InvoiceDetails(invoice);
            response.add(invoiceResponse);
        });
        return response;
    }

    @PostMapping(path = "accept/{id}")
    public InvoiceResponse acceptProForma(@RequestBody AccessTokenModel accessToken, @PathVariable long id){
        this.employeeRepository.findByToken(accessToken.getAccessToken());
        InvoiceBuffer invoiceBuffer = this.invoiceBufferRepository.getInvoiceBufferById(id);
        Invoice invoice = new Invoice(invoiceBuffer);
        String invoiceNumber = this.invoicesRepository.generateInvoiceNumber();
        invoice.setInvoiceNumber(invoiceNumber);
        this.invoicesRepository.persist(invoice);
        this.invoiceService.addPositions(invoice, invoiceBuffer.getVisit());
        this.invoicesRepository.update(invoice);
        return new InvoiceResponse(accessToken.getAccessToken(), invoice);
    }

    @PostMapping(path = "details/{id}")
    public InvoiceResponse getDetails(@RequestBody AccessTokenModel accessToken, @PathVariable long id){
        this.employeeRepository.findByToken(accessToken.getAccessToken());
        Invoice invoice = this.invoicesRepository.getInvoiceById(id);
        return new InvoiceResponse(accessToken.getAccessToken(), invoice);
    }
}
