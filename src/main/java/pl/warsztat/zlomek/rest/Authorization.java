package pl.warsztat.zlomek.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.exceptions.FieldsNotCorrect;
import pl.warsztat.zlomek.exceptions.UserExistException;
import pl.warsztat.zlomek.exceptions.UserNotFoundException;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.request.RegisterModel;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.model.response.AccessTokenResponse;
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

    @RequestMapping(value = "/getClientData/{token}", method = RequestMethod.GET)
    public Client getClient(@PathVariable(name = "token") String token){
        return clientRepository.findByToken(token);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Client registerClient(@RequestBody RegisterModel model){
        try {
            clientRepository.findClientByUsername(model.getEmail());
            throw new UserExistException("Użytkownik o podanym adresie e-mail istnieje");
        }catch (UserNotFoundException e){
            if(!model.getConfirmPassword().equals(model.getPassword())){
                throw new FieldsNotCorrect(new String[]{"Hasła się nie zgadzają"});
            }
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            Client client = new Client(model.getFirstName(), model.getLastName(), model.getEmail(), model.getPhoneNumber(),
                    model.getCityName(), model.getStreetName(), model.getBuildNum(), model.getAptNum(), model.getZipCode(),
                    model.getPassword());
            clientRepository.insert(client);
            return client;
        }
    }
    @RequestMapping(method = RequestMethod.POST, path = "/signIn")
    public AccessTokenResponse signIn(@RequestBody SignInRequest request){
        Client client = clientRepository.signIn(request.getEmail(), request.getPassword());
        return new AccessTokenResponse(clientRepository.generateToken(client));
    }
}
