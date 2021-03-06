package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CarServiceDataRepository;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.CarServiceData;
import pl.warsztat.zlomek.model.db.Company;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.request.AddCompanyRequest;
import pl.warsztat.zlomek.model.response.CompanyDetailsResponse;
import pl.warsztat.zlomek.model.response.CompanyListResponse;
import pl.warsztat.zlomek.model.response.CompanyResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/rest/companies")
public class CompaniesController {

    private EmployeeRepository employeeRepository;
    private CompaniesRepository companiesRepository;
    private CarServiceDataRepository carServiceDataRepository;

    @Autowired
    public CompaniesController(EmployeeRepository employeeRepository, CompaniesRepository companiesRepository,
                               CarServiceDataRepository carServiceDataRepository) {
        this.employeeRepository = employeeRepository;
        this.companiesRepository = companiesRepository;
        this.carServiceDataRepository = carServiceDataRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addCompany(@RequestBody AddCompanyRequest companyRequest) {
        try {
            employeeRepository.findByToken(companyRequest.getAccessToken());
            companiesRepository.getCompanyByNip(companyRequest.getNip());
        } catch (ResourcesNotFoundException e) {
            Company company = new Company(companyRequest.getNip(), companyRequest.getEmail(), companyRequest.getCompanyName(),
                    companyRequest.getCityName(), companyRequest.getStreetName(), companyRequest.getBuildingNum(),
                    companyRequest.getAptNum(), companyRequest.getZipCode());
            companiesRepository.createCompany(company);
        }
        return new AccessTokenModel(companyRequest.getAccessToken());
    }

    @PostMapping(path = "editCompany")
    public AccessTokenModel editCompany(@RequestBody AddCompanyRequest companyRequest) {
        Company company = companiesRepository.getCompanyByNip(companyRequest.getNip());
        company.setNip(companyRequest.getNip());
        company.setEmail(companyRequest.getEmail());
        company.setCompanyName(companyRequest.getCompanyName());
        company.setCityName(companyRequest.getCityName());
        company.setStreetName(companyRequest.getStreetName());
        company.setBuildingNum(companyRequest.getBuildingNum());
        company.setAptNum(companyRequest.getAptNum());
        company.setZipCode(companyRequest.getZipCode());
        this.companiesRepository.updateCompany(company);

        return new AccessTokenModel(companyRequest.getAccessToken());
    }

    @PostMapping(path = "{id}")
    public CompanyResponse getCompanyById(@PathVariable long id, @RequestBody AccessTokenModel accessToken) {
        this.employeeRepository.findByToken(accessToken.getAccessToken());
        return new CompanyDetailsResponse(this.companiesRepository.getCompanyId(id), accessToken.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "carService")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addCarServiceData(@RequestBody AddCompanyRequest companyRequest) {
        employeeRepository.findByToken(companyRequest.getAccessToken());
        CarServiceData carServiceData = new CarServiceData(companyRequest.getNip(), companyRequest.getEmail(), companyRequest.getCompanyName(),
                companyRequest.getCityName(), companyRequest.getStreetName(), companyRequest.getBuildingNum(),
                companyRequest.getAptNum(), companyRequest.getZipCode());
        carServiceDataRepository.save(carServiceData);
        return new AccessTokenModel(companyRequest.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "getCompanies")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyListResponse getCompaniesList(@RequestBody AccessTokenModel accessToken){
        Employee employee = employeeRepository.findByToken(accessToken.getAccessToken());
        List<Company> companies = companiesRepository.getCompanies();
        ArrayList<CompanyResponse> companiesList = new ArrayList<>();
        companies.forEach(company -> companiesList.add(new CompanyResponse(company)));

        return new CompanyListResponse(employee.getAccessToken().toString(), companiesList);
    }
}
