package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.warsztat.zlomek.data.ClientRepository;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.ClientStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/users")
public class UsersController {

    @Autowired
    public UsersController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    private ClientRepository clientRepository;

    @RequestMapping
    public String getUsersList(Model model){
        List<Client> clients = clientRepository.getAllClients();
        List<Client> active = clients.stream().filter((client -> client.getStatus().equals(ClientStatus.ACTIVE))).
                collect(Collectors.toList());
        model.addAttribute("activeAccounts", active);
        List<Client> banned = clients.stream().filter((client -> client.getStatus().equals(ClientStatus.BANNED))).
                collect(Collectors.toList());
        model.addAttribute("bannedAccounts", banned);
        List<Client> removed = clients.stream().filter((client -> client.getStatus().equals(ClientStatus.REMOVED))).
                collect(Collectors.toList());
        model.addAttribute("removedAccounts", removed);
        return "users";
    }

    @RequestMapping(path = "clientDetails", method = RequestMethod.GET)
    public String getClientDetails(Model model, @RequestParam long clientId){
        model.addAttribute("account", clientRepository.getClientById(clientId));
        return "clientDetails";
    }

    @RequestMapping(path = "update", method = RequestMethod.POST)
    public String updateClientData(@Valid Client client){
        return "users";
    }
}
