package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.CompaniesHasEmployees;
import pl.warsztat.zlomek.model.db.Company;

@Service
public class CompanyService {

    private CompaniesRepository companiesRepository;

    @Autowired
    public CompanyService(CompaniesRepository companiesRepository){
        this.companiesRepository = companiesRepository;
    }

    public Company getClientCompany(Client client, long id){
        CompaniesHasEmployees che = client.getCompanies().stream().filter((companiesHasEmployees ->
                companiesHasEmployees.getCompany().getId() == id)).findFirst().get();
        return che.getCompany();
    }
}
