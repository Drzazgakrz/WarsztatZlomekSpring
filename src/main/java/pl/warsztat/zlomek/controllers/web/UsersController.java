package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
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

    @GetMapping("clientDetails")
    public String getClientDetails(Model model, @RequestParam long clientId){
        model.addAttribute("account", clientRepository.getClientById(clientId));
        return "clientDetails";
    }

    @PostMapping(path = "update")
    public String updateClientData(Client client, Model model){
        System.out.println(client.getClientId());
        try {
            Client current = this.clientRepository.getClientById(client.getClientId());
            current.cloneClient(client);
            this.clientRepository.update(current);
            return "redirect:/users";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("account", client);
            return "redirect:/users/clientDetails?clientId="+client.getClientId();
        }
    }
}
