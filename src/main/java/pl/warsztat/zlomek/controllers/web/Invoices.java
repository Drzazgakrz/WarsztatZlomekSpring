package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.warsztat.zlomek.data.InvoicesRepository;

@Controller
@RequestMapping(path = "/invoices")
public class Invoices {

    private InvoicesRepository invoicesRepository;
    @Autowired
    public Invoices(InvoicesRepository invoicesRepository) {
        this.invoicesRepository = invoicesRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/vat")
    public String getVatInvoices(Model model){
        model.addAttribute("invoices", invoicesRepository.getInvoices());
        return "invoices";
    }
}
