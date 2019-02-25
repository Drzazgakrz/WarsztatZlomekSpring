package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.service.AuthorizationService;

import java.util.*;
@Controller
@RequestMapping(path = "")
public class IndexController {

    private AuthorizationService authorizationService;

    @Autowired
    public IndexController(AuthorizationService authorizationService){
        this.authorizationService = authorizationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showSignInForm(){
        return "login";
    }

    /*@RequestMapping(path = "/signIn", method = RequestMethod.POST,
            consumes="application/json",headers = "content-type=application/x-www-form-urlencoded")
    public String signIn(SignInRequest request, RedirectAttributes model){
        model.addFlashAttribute("accessToken",authorizationService.signIn(request));
        return "redirect:/index";
    }*/

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String index(Model model){
        return "index";
    }

    @RequestMapping(path = "/index/token", method = RequestMethod.POST)
    public String index(String accessToken, RedirectAttributes model){
        try {
            authorizationService.signIn(accessToken);
            model.addFlashAttribute("accessToken",accessToken);
            return "redirect:/index";
        }catch (Exception e){
            return "redirect:/";
        }
    }
}
