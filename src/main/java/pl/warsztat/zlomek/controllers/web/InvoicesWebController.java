package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.warsztat.zlomek.data.InvoiceBufferRepository;
import pl.warsztat.zlomek.data.InvoicesRepository;

@Controller
@RequestMapping(path = "/invoices")
public class InvoicesWebController {

    private InvoicesRepository invoicesRepository;
    private InvoiceBufferRepository invoiceBufferRepository;

    @Autowired
    public InvoicesWebController(InvoicesRepository invoicesRepository, InvoiceBufferRepository invoiceBufferRepository) {
        this.invoicesRepository = invoicesRepository;
        this.invoiceBufferRepository = invoiceBufferRepository;
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
}
