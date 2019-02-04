package pl.warsztat.zlomek.rest;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CarsHasOwners;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarsHasOwnersRepository {

    @PersistenceContext
    private EntityManager em;

    public void insertOwnership(CarsHasOwners carsHasOwners){
        em.persist(carsHasOwners);
    }
}
