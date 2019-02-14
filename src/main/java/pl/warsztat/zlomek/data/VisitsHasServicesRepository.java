package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.VisitsHasServices;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VisitsHasServicesRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(VisitsHasServices vhs){
        this.em.persist(vhs);
    }
}
