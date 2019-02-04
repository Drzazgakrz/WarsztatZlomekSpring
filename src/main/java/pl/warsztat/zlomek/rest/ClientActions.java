package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.request.ClientForm;
import pl.warsztat.zlomek.model.response.ClientDataResponse;
import pl.warsztat.zlomek.service.ClientRepository;

@RestController
@RequestMapping(path = "/client")
public class ClientActions {

    private ClientRepository clientRepository;

    @Autowired
    public ClientActions(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    @RequestMapping(path = "/{accessToken}",method = RequestMethod.GET)
    public ClientDataResponse getClientByToken(@PathVariable(name = "accessToken") String accessToken){
        return new ClientDataResponse(clientRepository.findByToken(accessToken), accessToken);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public HttpStatus updateClientData(@RequestBody ClientForm clientForm){
        Client client = clientRepository.findClientByUsername(clientForm.getEmail());
        client.setAptNum(clientForm.getAptNum());
        client.setBuildNum(clientForm.getBuildNum());
        client.setCityName(clientForm.getCityName());
        client.setPhoneNumber(clientForm.getPhoneNumber());
        client.setStreetName(clientForm.getStreetName());
        client.setZipCode(clientForm.getZipCode());
        client.setFirstName(clientForm.getFirstName());
        client.setLastName(clientForm.getLastName());
        clientRepository.update(client);
        return HttpStatus.OK;
    }
}
