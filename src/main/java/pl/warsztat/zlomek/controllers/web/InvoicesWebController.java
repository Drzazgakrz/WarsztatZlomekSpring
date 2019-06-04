package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.*;
import pl.warsztat.zlomek.model.db.MethodOfPayment;
import pl.warsztat.zlomek.model.request.AddInvoiceRequest;
import pl.warsztat.zlomek.service.InvoiceService;

@Controller
@RequestMapping(path = "/invoices")
public class InvoicesWebController {

    private InvoicesRepository invoicesRepository;
    private InvoiceBufferRepository invoiceBufferRepository;
    private CompaniesRepository companiesRepository;
    private VisitRepository visitRepository;
    private InvoiceService invoiceService;

    @Autowired
    public InvoicesWebController(InvoicesRepository invoicesRepository
            , InvoiceBufferRepository invoiceBufferRepository
            , CompaniesRepository companiesRepository
            , VisitRepository visitRepository
            , InvoiceService invoiceService) {
        this.invoicesRepository = invoicesRepository;
        this.invoiceBufferRepository = invoiceBufferRepository;
        this.companiesRepository = companiesRepository;
        this.visitRepository = visitRepository;
        this.invoiceService = invoiceService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/vat")
    public String getVatInvoices(Model model){
        model.addAttribute("invoices", invoicesRepository.getInvoices());
        return "invoices";
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getInvoiceDetails(@PathVariable(name = "id") long invoiceId, Model model){
        model.addAttribute("invoice", invoicesRepository.getInvoiceById(invoiceId));
        return "invoiceDetails";
    }

    @RequestMapping(path="/pro_forma", method = RequestMethod.GET)
    public String getProFormaInvoices(Model model){
        model.addAttribute("invoices", invoiceBufferRepository.getInvoices());
        return "invoices";
    }

    @GetMapping(path = "/add")
    public String addInvoice(Model model){
        model.addAttribute("companies", this.companiesRepository.getCompanies());
        model.addAttribute("visits", this.visitRepository.getAllVisits());
        model.addAttribute("methods", MethodOfPayment.values());
        return "addInvoice";
    }

    @PostMapping(path = "save")
    public String saveInvoice(AddInvoiceRequest request){
        try {
            System.out.println(request.getVisitId() + " " + request.getCompanyName() + " " + request.getDiscount() + " " + request.getMethodOfPayment());
            this.invoiceService.createVatInvoice(request);
            return "redirect:/invoices/vat";
        } catch (Exception e){
            return "redirect:/invoices/add";
        }
    }
}
