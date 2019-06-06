package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.CompaniesHasEmployeesRepository;
import pl.warsztat.zlomek.data.CompaniesRepository;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.*;
import pl.warsztat.zlomek.model.request.AddClientToCompanyRequest;
import pl.warsztat.zlomek.model.request.BanClientModel;
import pl.warsztat.zlomek.model.request.ClientForm;
import pl.warsztat.zlomek.model.request.RemoveClientFromCompanyRequest;
import pl.warsztat.zlomek.model.response.ClientDataResponse;
import pl.warsztat.zlomek.data.ClientRepository;

@RestController
@RequestMapping(path = "/rest/client")
public class ClientsController {

    private ClientRepository clientRepository;
    private EmployeeRepository employeeRepository;
    private CompaniesRepository companiesRepository;
    private CompaniesHasEmployeesRepository companiesHasEmployeesRepository;

    @Autowired
    public ClientsController(ClientRepository clientRepository, EmployeeRepository employeeRepository,
                             CompaniesRepository companiesRepository, CompaniesHasEmployeesRepository cheRepository){
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.companiesRepository = companiesRepository;
        this.companiesHasEmployeesRepository = cheRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ClientDataResponse getClientByToken(@RequestBody AccessTokenModel accessToken){
        return new ClientDataResponse(clientRepository.findByToken(accessToken.getAccessToken()), accessToken.getAccessToken());
    }

    @PostMapping(path = "/{id}")
    public ClientDataResponse getClientData(@RequestBody AccessTokenModel accessToken, @PathVariable long id){
        this.employeeRepository.findByToken(accessToken.getAccessToken());
        return new ClientDataResponse(clientRepository.getClientById(id), accessToken.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.PUT)
    public AccessTokenModel updateClientData(@RequestBody ClientForm clientForm){
        Client client = clientRepository.findClientByUsername(clientForm.getEmail());
        client.setAptNum(clientForm.getAptNum());
        client.setBuildNum(clientForm.getBuildNum());
        client.setCityName(clientForm.getCityName());
        client.setPhoneNumber(clientForm.getPhoneNumber());
        client.setStreetName(clientForm.getStreetName());
        client.setZipCode(clientForm.getZipCode());
        client.setFirstName(clientForm.getFirstName());
        client.setLastName(clientForm.getLastName());
        this.clientRepository.update(client);
        return new AccessTokenModel(clientForm.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/addToCompany")
    @ResponseStatus(HttpStatus.CREATED)
    public AccessTokenModel addClientToCompany(@RequestBody AddClientToCompanyRequest request){
        this.employeeRepository.findByToken(request.getAccessToken());
        Client client = this.clientRepository.findClientByUsername(request.getClientUsername());
        Company company = this.companiesRepository.getCompanyId(request.getCompanyId());
        CompaniesHasEmployees companiesHasEmployees = company.getEmployees().stream().filter(che->
                che.getClient().equals(client)).findFirst().orElse(null);
        if(companiesHasEmployees != null){
            companiesHasEmployees.setStatus(EmployeeStatus.CURRENT_EMPLOYER);
            this.companiesHasEmployeesRepository.update(companiesHasEmployees);
        }else {
            CompaniesHasEmployees che = company.addClientToCompany(client);
            client.addCompany(che);
            company.getEmployees().add(che);
            this.companiesRepository.updateCompany(company);
            this.clientRepository.update(client);
            this.companiesHasEmployeesRepository.persist(che);
        }
        return new AccessTokenModel(request.getAccessToken());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/removeFromCompany")
    public AccessTokenModel removeClientFromCompany(@RequestBody RemoveClientFromCompanyRequest request){
        Client client = this.clientRepository.findByToken(request.getAccessToken());
        Company company = this.companiesRepository.getCompanyId(request.getCompanyId());
        CompaniesHasEmployees companiesHasEmployees = company.getEmployees().stream().filter(che->
                che.getClient().equals(client)).findFirst().orElse(null);
        if(companiesHasEmployees == null)
            throw new ResourcesNotFoundException("Klient nie jest przypisany do tej firmy", request.getAccessToken());
        companiesHasEmployees.setStatus(EmployeeStatus.FORMER_EMPLOYER);
        companiesHasEmployeesRepository.update(companiesHasEmployees);
        return new AccessTokenModel(request.getAccessToken());
    }

    @PostMapping(path = "remove")
    public void removeClient(@RequestBody AccessTokenModel accessTokenModel){
        Client client = this.clientRepository.findByToken(accessTokenModel.getAccessToken());
        client.setStatus(ClientStatus.REMOVED);
        this.clientRepository.update(client);
    }

    @PostMapping(path = "ban")
    public AccessTokenModel banClient(@RequestBody BanClientModel model){
        this.employeeRepository.findByToken(model.getAccessToken());
        Client client = this.clientRepository.findClientByUsername(model.getUsername());
        client.setStatus(ClientStatus.BANNED);
        this.clientRepository.update(client);
        return new AccessTokenModel(model.getAccessToken());
    }

}
