package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;
import pl.warsztat.zlomek.model.db.InvoiceBufferPosition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class InvoiceBufferPositionRepository {

    @PersistenceContext
    EntityManager em;

    public void persist(InvoiceBufferPosition ib){
        em.persist(ib);
    }

    public void update(InvoiceBufferPosition ib){
        em.merge(ib);
    }
}
