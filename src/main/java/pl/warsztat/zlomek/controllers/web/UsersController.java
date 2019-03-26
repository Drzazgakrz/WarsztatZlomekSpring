package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.warsztat.zlomek.data.ClientRepository;
import pl.warsztat.zlomek.model.db.ClientStatus;

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
        model.addAttribute("activeAccounts", clientRepository.getClientsByStatus(ClientStatus.ACTIVE));
        model.addAttribute("bannedAccounts", clientRepository.getClientsByStatus(ClientStatus.BANNED));
        model.addAttribute("removedAccounts", clientRepository.getClientsByStatus(ClientStatus.REMOVED));
        return "users";
    }

}
