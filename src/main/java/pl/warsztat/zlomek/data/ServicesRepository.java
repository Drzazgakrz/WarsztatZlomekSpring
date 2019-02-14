package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CarPart;
import pl.warsztat.zlomek.model.db.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ServicesRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Service> getAllServices(){
        return em.createQuery("SELECT service FROM Service service", Service.class).getResultList();
    }

    public void saveService(Service service){
        em.persist(service);
    }

    public Service getServiceById(long id){
        TypedQuery<Service> query = em.createQuery("SELECT service FROM Service service WHERE service.id=:id",
                Service.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public void updateService(Service service){
        em.merge(service);
    }
}
