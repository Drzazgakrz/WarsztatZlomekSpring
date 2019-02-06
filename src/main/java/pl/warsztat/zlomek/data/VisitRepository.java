package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.Visit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VisitRepository {

    @PersistenceContext
    private EntityManager em;

    public void saveVisit(Visit visit){
        em.persist(visit);
    }
}
