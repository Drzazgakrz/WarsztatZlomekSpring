package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.CompaniesHasCars;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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

    public CompaniesHasCars getCompanyCarRelationship(long companyId, long carId){
        try{
            TypedQuery<CompaniesHasCars> query = em.createQuery("SELECT companyHasCar from CompaniesHasCars companyHasCar " +
                    "WHERE companyHasCar.id.companyId=:companyId AND companyHasCar.id.carId=:carId", CompaniesHasCars.class);
            query.setParameter("companyId", companyId);
            query.setParameter("carId", carId);
            return query.getSingleResult();
        }catch (Exception e){}
        throw new ResourcesNotFoundException("Brak samochodu o podanym numerze vin");
    }
}
