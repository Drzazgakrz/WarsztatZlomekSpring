package pl.warsztat.zlomek.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.request.SignInRequest;

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
}
