package pl.warsztat.zlomek.controllers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.warsztat.zlomek.data.EmployeeRepository;
import pl.warsztat.zlomek.model.db.AccountType;
import pl.warsztat.zlomek.model.db.Employee;
import pl.warsztat.zlomek.model.db.EmployeeStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "employee")
public class EmployeesController {

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeesController(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public String employeesList(Model model){
        List<Employee> employees = this.employeeRepository.getAll();
        model.addAttribute("current", getEmployeebyStatus(employees, EmployeeStatus.CURRENT_EMPLOYER));
        model.addAttribute("former", getEmployeebyStatus(employees, EmployeeStatus.FORMER_EMPLOYER));
        return "employees";
    }

    private List<Employee> getEmployeebyStatus(List<Employee> employees, EmployeeStatus status){
        return employees.stream().filter(employee -> employee.getStatus().equals(status)).collect(Collectors.toList());
    }

    @GetMapping("add")
    public String addEmployee(Model model){
        model.addAttribute("employee", new Employee());
        model.addAttribute("accountTypes", AccountType.values());
        return "addEmployee";
    }

    @PostMapping(path = "add", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addEmployee(Employee employee){
        employee.setCreatedAt(LocalDateTime.now());
        employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        this.employeeRepository.insert(employee);
        return "redirect:/employee";
    }

    @GetMapping(path = "details")
    public String getEmployeeDetails(@RequestParam long employeeId, Model model){
        try {
            model.addAttribute("employee", this.employeeRepository.findById(employeeId));
            model.addAttribute("accountTypes", AccountType.values());
            return "employeeDetails";
        }catch (Exception e){
            return "employees";
        }
    }

    @PostMapping(path = "edit")
    public String editEmployee(Employee employee){
        try{
            Employee current = this.employeeRepository.findById(employee.getId());
            current.copy(employee);
            this.employeeRepository.update(current);
            return "redirect:/employee";
        }catch (Exception e){
            return "redirect:/employee/details?employeeId="+employee.getId();
        }
    }
}
