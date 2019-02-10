package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Company;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.db.EmployeeToken;
import pl.warsztat.zlomek.model.request.AddCompanyRequest;

@RestController
@RequestMapping(path = "/companies")
public class CompaniesController {

    private EmployeeRepository employeeRepository;
    private CompaniesRepository companiesRepository;

    @Autowired
    public CompaniesController(EmployeeRepository employeeRepository, CompaniesRepository companiesRepository) {
        this.employeeRepository = employeeRepository;
        this.companiesRepository = companiesRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addCompany(@RequestBody AddCompanyRequest companyRequest) {
        try {
            employeeRepository.findByToken(companyRequest.getAccessToken());
            companiesRepository.getCompanyByNip(companyRequest.getNip());
        } catch (ResourcesNotFoundException e) {
            Company company = new Company(companyRequest.getNip(), companyRequest.getEmail(), companyRequest.getName(),
                    companyRequest.getCityName(), companyRequest.getStreetName(), companyRequest.getBuildingNum(),
                    companyRequest.getAptNum(), companyRequest.getZipCode());
            companiesRepository.createCompany(company);
        }
        return new AccessTokenModel(companyRequest.getAccessToken());
    }
}
