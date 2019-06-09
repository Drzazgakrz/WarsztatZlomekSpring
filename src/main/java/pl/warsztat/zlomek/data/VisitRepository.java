package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.db.Visit;
import pl.warsztat.zlomek.model.db.VisitStatus;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

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
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit WHERE visit.id = :id ",
                    Visit.class);
            query.setParameter("id", visitId);
            return query.getSingleResult();
        }catch (NoResultException e){
            throw new ResourcesNotFoundException("Brak wizyty spełniającej podane kryteria");
        }
    }

    public Visit getVisitById(long visitId, VisitStatus visitStatus){
        try {
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit WHERE visit.id = :id " +
                    "AND visit.status=:visitStatus", Visit.class);
            query.setParameter("id", visitId);
            query.setParameter("visitStatus", visitStatus);
            return query.getSingleResult();
        }catch (NoResultException e){
            throw new ResourcesNotFoundException("Brak wizyty spełniającej podane kryteria");
        }
    }

    public void updateVisit(Visit visit){
        em.merge(visit);
    }

    public List<Visit> getVisitByStatus(VisitStatus status){
        try {
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit WHERE visit.status = :status" +
                            " ORDER BY visit.visitDate DESC",
                    Visit.class);
            query.setParameter("status", status);
            return query.getResultList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public List<Visit> getAllVisits(){
        try {
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit ORDER BY visit.visitDate DESC",
                    Visit.class);
            return query.getResultList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public void remove(Visit visit){
        this.em.remove(visit);
    }

    public List<Visit> getAllClientsVisits(Client client){
        try {
            TypedQuery<Visit> query = em.createQuery("SELECT visit FROM Visit visit WHERE visit.client = :client",
                    Visit.class);
            query.setParameter("client", client);
            return query.getResultList();
        }catch (NoResultException e){
            return new ArrayList<>();
        }
    }
}
