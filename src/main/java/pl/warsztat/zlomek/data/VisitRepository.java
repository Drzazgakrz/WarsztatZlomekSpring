package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.db.VisitStatus;

import javax.persistence.*;
import javax.transaction.Transactional;

@Repository
@Transactional
public class VisitRepository {

    @PersistenceContext
    private EntityManager em;

    public void saveVisit(Visit visit){
        em.persist(visit);
    }

    public Visit getVisitById(long visitId){
        try {
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit WHERE visit.id = :id AND " +
                            "visit.status=:newVisit", Visit.class);
            query.setParameter("id", visitId);
            query.setParameter("newVisit", VisitStatus.NEW);
            return query.getSingleResult();
        }catch (NoResultException e){
            throw new ResourcesNotFoundException("Brak wizyty spełniającej podane kryteria");
        }
    }

    public void updateVisit(Visit visit){
        em.merge(visit);
    }
}
