package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.service.AuthorizationService;

@Controller
@RequestMapping(path = "")
public class IndexController {

    private AuthorizationService authorizationService;

    @Autowired
    public IndexController(AuthorizationService authorizationService){
        this.authorizationService = authorizationService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/login")
    public String showSignInForm(){
        return "login";
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(){
        return "index";
    }
}
