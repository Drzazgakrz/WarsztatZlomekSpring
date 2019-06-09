package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.InvoiceBuffer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class InvoiceBufferRepository {
    @PersistenceContext
    EntityManager em;

    public void persist(InvoiceBuffer ib) {
        em.persist(ib);
    }

    public void update(InvoiceBuffer ib) {
        em.merge(ib);
    }

    public List<InvoiceBuffer> getInvoices() {
        TypedQuery<InvoiceBuffer> query = em.createQuery("SELECT invoice FROM InvoiceBuffer invoice", InvoiceBuffer.class);
        return query.getResultList();
    }

    public InvoiceBuffer getInvoiceBufferById(long id){
        try {
            TypedQuery<InvoiceBuffer> query = em.createQuery("SELECT invoice FROM InvoiceBuffer invoice" +
                    " where invoice.id=:id", InvoiceBuffer.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }catch (NoResultException e){
            throw new ResourcesNotFoundException("Brak faktury o podanym id");
        }
    }
}
