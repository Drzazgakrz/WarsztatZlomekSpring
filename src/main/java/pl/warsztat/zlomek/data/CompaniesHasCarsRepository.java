package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CompaniesHasCars;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CompaniesHasCarsRepository {

    @PersistenceContext
    EntityManager em;

    public void saveCompaniesCarsRelationship(CompaniesHasCars chc){
        em.persist(chc);
    }
}
