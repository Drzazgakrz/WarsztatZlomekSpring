package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.CarBrand;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarBrandRepository {

    @PersistenceContext
    EntityManager em;

    public CarBrand getCarBrandByName(String brandName){
        try {
            TypedQuery<CarBrand> query = em.createQuery("SELECT carBrand FROM CarBrand carBrand WHERE " +
                    "carBrand.brandName = :brandName",CarBrand.class);
            query.setParameter("brandName", brandName);
            return query.getSingleResult();
        }catch (Exception e){}
        throw new ResourcesNotFoundException("Marka o tej nazwie nie istnieje");
    }

    public void save(CarBrand carBrand){
        em.merge(carBrand);
    }
}
