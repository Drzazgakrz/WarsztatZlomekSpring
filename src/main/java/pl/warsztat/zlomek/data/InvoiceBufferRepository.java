package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class InvoiceBufferRepository {
    @PersistenceContext
    EntityManager em;

    public void persist(InvoiceBuffer ib){
        em.persist(ib);
    }

    public void update(InvoiceBuffer ib){
        em.merge(ib);
    }
}
