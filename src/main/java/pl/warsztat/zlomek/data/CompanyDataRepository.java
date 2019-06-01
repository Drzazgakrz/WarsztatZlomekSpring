package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CompanyData;
import pl.warsztat.zlomek.service.CompanyService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompanyDataRepository {
    @PersistenceContext
    EntityManager em;

    public void persist(CompanyData data){
        em.persist(data);
    }

    public List<CompanyData> getAllCompanies(){
        return em.createQuery("SELECT data FROM CompanyData data",CompanyData.class).getResultList();
    }
}
