package pl.warsztat.zlomek.controllers.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.request.ClientForm;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.data.ClientRepository;

@RestController
@RequestMapping(path = "/rest/authorization")
public class AuthorizationController {
    private ClientRepository clientRepository;
    private EmployeeRepository employeeRepository;

    private Logger log;
    @Autowired
    public AuthorizationController(ClientRepository clientRepository, Logger log, EmployeeRepository employeeRepository){
        this.clientRepository = clientRepository;
        this.log = log;
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Client registerClient(@RequestBody ClientForm model){
            clientRepository.findClientByUsername(model.getEmail());
            if(!model.getConfirmPassword().equals(model.getPassword())){
                throw new FieldsNotCorrect(new String[]{"Hasła się nie zgadzają"});
            }
            Client client = new Client(model);
            clientRepository.insert(client);
            return client;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/signIn")
    public AccessTokenModel signIn(@RequestBody SignInRequest request){
        Client client = clientRepository.signIn(request.getEmail(), request.getPassword());
        return new AccessTokenModel(clientRepository.generateToken(client));
    }

    @RequestMapping(method = RequestMethod.POST, path = "checkToken")
    public void checkToken(@RequestBody AccessTokenModel model){
        this.employeeRepository.findByToken(model.getAccessToken());
    }
}
