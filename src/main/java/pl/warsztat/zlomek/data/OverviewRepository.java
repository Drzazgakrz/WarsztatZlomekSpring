package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.Overview;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class OverviewRepository {

    @PersistenceContext
    private EntityManager em;

    public void saveOverview(Overview overview){
        em.persist(overview);
    }

    public void updateOverview(Overview overview){
        em.merge(overview);
    }
}
