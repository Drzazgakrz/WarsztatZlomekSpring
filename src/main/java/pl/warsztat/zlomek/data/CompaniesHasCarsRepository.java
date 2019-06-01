package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.model.db.CompaniesHasCars;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CompaniesHasCarsRepository {

    @PersistenceContext
    EntityManager em;

    public void saveCompaniesCarsRelationship(CompaniesHasCars chc){
        try {
            em.persist(chc);
        }catch (EntityExistsException e){
            throw new ResourcesExistException("Samochód jest już przypisany do tej firmy");
        }
    }
}
