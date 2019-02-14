package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.VisitsParts;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VisitsHasPartsRepository {

    @PersistenceContext
    private EntityManager em;

    public void persist(VisitsParts vp){
        em.persist(vp);
    }
}
