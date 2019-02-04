package pl.warsztat.zlomek.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.UserExistException;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.request.ClientForm;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.service.ClientRepository;

@RestController
@RequestMapping(path = "/authorization")
public class Authorization {
    private ClientRepository clientRepository;

    private Logger log;
    @Autowired
    public Authorization(ClientRepository clientRepository, Logger log){
        this.clientRepository = clientRepository;
        this.log = log;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Client registerClient(@RequestBody ClientForm model){
        try {
            clientRepository.findClientByUsername(model.getEmail());
            throw new UserExistException("Użytkownik o podanym adresie e-mail istnieje");
        }catch (ResourcesNotFoundException e){
            if(!model.getConfirmPassword().equals(model.getPassword())){
                throw new FieldsNotCorrect(new String[]{"Hasła się nie zgadzają"});
            }
            Client client = new Client(model);
            clientRepository.insert(client);
            return client;
        }
    }
    @RequestMapping(method = RequestMethod.POST, path = "/signIn")
    public AccessTokenModel signIn(@RequestBody SignInRequest request){
        Client client = clientRepository.signIn(request.getEmail(), request.getPassword());
        return new AccessTokenModel(clientRepository.generateToken(client));
    }
}
