package pl.warsztat.zlomek.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.model.AccessTokenModel;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.request.SignInRequest;

@Service
public class AuthorizationService {

    EmployeeRepository employeeRepository;

    @Autowired
    public AuthorizationService(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public String signIn(SignInRequest request){
        Employee employee = employeeRepository.signIn(request.getEmail(), request.getPassword());
        return this.employeeRepository.generateToken(employee);
    }

    public String signIn(String accessToken){
        Employee employee = employeeRepository.findByToken(accessToken);
        return this.employeeRepository.generateToken(employee);
    }
}
