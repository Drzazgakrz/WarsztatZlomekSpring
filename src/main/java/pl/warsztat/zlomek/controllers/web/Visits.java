package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.warsztat.zlomek.data.VisitRepository;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.db.VisitStatus;
import pl.warsztat.zlomek.service.AuthorizationService;
import pl.warsztat.zlomek.service.VisitService;

import java.util.List;

@Controller
@RequestMapping(path = "/visits")
public class Visits {
    private VisitRepository visitRepository;
    private AuthorizationService authorizationService;
    private VisitService visitService;
    @Autowired
    public Visits(VisitRepository visitRepository, AuthorizationService authorizationService, VisitService visitService){
        this.visitRepository = visitRepository;
        this.authorizationService = authorizationService;
        this.visitService = visitService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getVisitsByStatus(@RequestParam String visitStatus, Model model){
        VisitStatus status = visitService.getStatus(visitStatus);
        List<Visit> visits = this.visitRepository.getVisitByStatus(status);
        model.addAttribute("visits", visits);
        model.addAttribute("title", visitService.getTitleByStatus(status));
        return "visits";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public String getAllVisits(Model model){
        List<Visit> visits = this.visitRepository.getAllVisits();
        model.addAttribute("visits", visits);
        model.addAttribute("title", "Wszystkie wizyty");
        return "visits";
    }
}
