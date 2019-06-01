package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

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

    public List<InvoiceBuffer> getInvoices(){
        TypedQuery<InvoiceBuffer> query = em.createQuery("SELECT invoice FROM InvoiceBuffer invoice", InvoiceBuffer.class);
        return query.getResultList();
    }
}
