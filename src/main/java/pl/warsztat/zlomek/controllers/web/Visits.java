package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.warsztat.zlomek.data.VisitRepository;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.db.VisitStatus;
import pl.warsztat.zlomek.service.AuthorizationService;

import java.util.List;

@Controller
@RequestMapping(path = "/visits")
public class Visits {
    private VisitRepository visitRepository;
    private AuthorizationService authorizationService;
    @Autowired
    public Visits(VisitRepository visitRepository, AuthorizationService authorizationService){
        this.visitRepository = visitRepository;
        this.authorizationService = authorizationService;
    }

    @RequestMapping(path = "/in_progress", method = RequestMethod.GET)
    public String getInProgressVisits(Model model){
        List<Visit> visits = this.visitRepository.getVisitByStatus(VisitStatus.IN_PROGRESS);
        model.addAttribute("visits", visits);
        model.addAttribute("title", "Wizyty w trakcie wykonywania");
        return "visits";
    }

    @RequestMapping(path = "/show_in_progress", method = RequestMethod.POST)
    public String index(String accessToken, RedirectAttributes model){
        try {
            authorizationService.signIn(accessToken);
            model.addFlashAttribute("accessToken",accessToken);
            return "redirect:/index";
        }catch (Exception e){
            return "redirect:/visits/in_progress";
        }
    }
}
