package pl.warsztat.zlomek.controllers.web;

import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.model.db.Company;

@Controller
@RequestMapping("companies")
public class CompaniesWebController {
    private CompaniesRepository companiesRepository;

    @Autowired
    public CompaniesWebController(CompaniesRepository companiesRepository){
        this.companiesRepository = companiesRepository;
    }

    @GetMapping
    public String getCompanies(Model model){
        model.addAttribute("companies", this.companiesRepository.getCompanies());
        return "companies";
    }

    @GetMapping("details")
    public String getCompanyDetails(@RequestParam long companyId, Model model){
        model.addAttribute("company",this.companiesRepository.getCompanyId(companyId));
        return "company";
    }

    @PostMapping("edit")
    public String editCompany(Company company){
        try {
            Company current = this.companiesRepository.getCompanyId(company.getId());
            current.copy(company);
            this.companiesRepository.updateCompany(current);
            return "redirect:/companies";
        } catch (Exception e){
            return "redirect:/companies/details?companyId="+company.getId();
        }
    }

    @GetMapping("add")
    public String addCompany(Model model){
        model.addAttribute("company", new Company());
        return "addCompany";
    }

    @PostMapping("add")
    public String addCompany(Company company){
        try {
            this.companiesRepository.updateCompany(company);
            return "redirect:/companies";
        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/companies/add";
        }
    }
}
