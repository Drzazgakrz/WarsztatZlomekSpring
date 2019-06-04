package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.model.db.CarsHasOwners;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarsHasOwnersRepository {

    @PersistenceContext
    private EntityManager em;

    public void insertOwnership(CarsHasOwners carsHasOwners){
        try {
            em.persist(carsHasOwners);
        }catch (EntityExistsException e){
            throw new ResourcesExistException("Podany samoch\u00D3d jest ju\u017C powi\u0105zany do tego klienta");
        }

    }

    public void updateOwnership(CarsHasOwners carsHasOwners){em.merge(carsHasOwners);}
}
