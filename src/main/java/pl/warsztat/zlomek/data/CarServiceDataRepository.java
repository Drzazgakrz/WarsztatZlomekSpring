package pl.warsztat.zlomek.data;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.CarServiceData;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarServiceDataRepository {
    @PersistenceContext
    private EntityManager em;

    Logger log;
    @Autowired
    public CarServiceDataRepository(Logger log){
        this.log = log;
    }

    public CarServiceData getCurrentData(){
        log.info(em.toString());
        TypedQuery<CarServiceData> query = em.createQuery("SELECT data FROM CarServiceData data " +
                "GROUP BY data.id HAVING data.id=MAX(id)", CarServiceData.class);
        return query.getSingleResult();
    }
}
