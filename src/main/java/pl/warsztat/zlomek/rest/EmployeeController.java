package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.request.SignInRequest;
import pl.warsztat.zlomek.model.response.VisitResponse;
import pl.warsztat.zlomek.model.response.VisitsList;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "employee")
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public AccessTokenModel signIn(@RequestBody SignInRequest signInRequest){
        Employee employee = employeeRepository.signIn(signInRequest.getEmail(), signInRequest.getPassword());
        return new AccessTokenModel(employeeRepository.generateToken(employee));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/visits")
    public VisitsList getEmployeesVisits(@RequestHeader String accessToken){
        Employee employee = employeeRepository.findByToken(accessToken);
        ArrayList<VisitResponse> visits = new ArrayList<>();
        employee.getVisits().forEach(visit -> visits.add(new VisitResponse(visit)));
        return new VisitsList(accessToken, visits);
    }
}
