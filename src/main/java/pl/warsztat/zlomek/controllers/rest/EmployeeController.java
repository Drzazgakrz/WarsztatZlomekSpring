package pl.warsztat.zlomek.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.model.response.VisitResponse;
import pl.warsztat.zlomek.model.response.VisitsList;
import pl.warsztat.zlomek.service.AuthorizationService;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/rest/employee")
public class EmployeeController {

    private EmployeeRepository employeeRepository;
    private AuthorizationService authorizationService;


    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, AuthorizationService authorizationService){
        this.employeeRepository = employeeRepository;
        this.authorizationService = authorizationService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public AccessTokenModel signIn(@RequestBody SignInRequest signInRequest){
        return new AccessTokenModel(authorizationService.signIn(signInRequest));
    }

    @RequestMapping(method = RequestMethod.POST, path = "/visits")
    public VisitsList getEmployeesVisits(@RequestBody AccessTokenModel accessToken){
        Employee employee = employeeRepository.findByToken(accessToken.getAccessToken());
        ArrayList<VisitResponse> visits = new ArrayList<>();
        employee.getVisits().forEach(visit -> visits.add(new VisitResponse(visit)));
        return new VisitsList(accessToken.getAccessToken(), visits);
    }
}
