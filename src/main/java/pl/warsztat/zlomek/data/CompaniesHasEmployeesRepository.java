package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CompaniesHasEmployees;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CompaniesHasEmployeesRepository {

    @PersistenceContext
    private EntityManager em;

    public void persist(CompaniesHasEmployees che){
        em.persist(che);
    }

    public void update(CompaniesHasEmployees che){
        em.merge(che);
    }
}
