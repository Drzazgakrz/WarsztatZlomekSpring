package pl.warsztat.zlomek.data;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class EmployeeRepository extends AccountRepository<Employee>{

    @PersistenceContext
    private EntityManager em;

    @Override
    public String generateToken(Employee account) {
        String token = createToken(account);
        EmployeeToken employeeToken = new EmployeeToken(token, LocalDateTime.now().plusMinutes(20), account);
        em.persist(employeeToken);
        account.getAccessToken().add(employeeToken);
        update(account);
        return token;
    }

    @Override
    public Employee findByToken(String accessToken) {
        try {
            TypedQuery<EmployeeToken> query = em.createQuery("SELECT employeeToken FROM EmployeeToken employeeToken " +
                    "WHERE employeeToken.accessToken = :accessToken", EmployeeToken.class);
            query.setParameter("accessToken", accessToken);
            EmployeeToken token =  query.getSingleResult();
            if(token.getExpiration().isAfter(LocalDateTime.now())){
                token.setExpiration(LocalDateTime.now().plusMinutes(20));
                em.merge(token);
                return token.getEmployee();
            }
        }catch (Exception e){
        }
        throw new ResourcesNotFoundException("Pracownik o podanym tokenie nie istnieje bądź token wygasł");
    }

    public Employee signIn(String username, String password){
        try {
            TypedQuery<Employee> query = em.createQuery("SELECT employee FROM Employee employee "+
                    "WHERE employee.email = :username",Employee.class);
            query.setParameter("username", username);
            Employee employee = query.getSingleResult();
            if(new BCryptPasswordEncoder().matches(password, employee.getPassword()))
                return employee;
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new ResourcesNotFoundException("Brak pracownika o podanych danych");
    }

    public List<Employee> getAll(){
        return em.createQuery("SELECT employee FROM Employee employee ", Employee.class).getResultList();
    }

    public Employee findById(long id){
        TypedQuery<Employee> query = em.createQuery("SELECT employee FROM Employee employee "+
                "WHERE employee.id = :id",Employee.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }
}
