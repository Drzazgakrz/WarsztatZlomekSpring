package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.model.db.CarPart;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CarPartsRepository {
    @PersistenceContext
    private EntityManager em;

    public List<CarPart> getCarParts(){
        try{
            TypedQuery<CarPart> query = em.createQuery("SELECT carPart FROM CarPart carPart", CarPart.class);
            return query.getResultList();
        }catch (NoResultException e){}
        return new ArrayList<>();
    }

    public void saveCarPart(CarPart carPart){
        try{
            em.persist(carPart);
        }catch (EntityExistsException e){
            throw new ResourcesExistException("Podana część istnieje");
        }

    }

    public CarPart getCarPartById(long id){
        TypedQuery<CarPart> query = em.createQuery("SELECT carPart FROM CarPart carPart WHERE carPart.id=:id",
                CarPart.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public void updateCarPart(CarPart carPart){
        em.merge(carPart);
    }
}
