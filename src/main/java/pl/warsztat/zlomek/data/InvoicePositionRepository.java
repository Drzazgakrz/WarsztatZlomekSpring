package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.model.db.InvoicePosition;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class InvoicePositionRepository {

    @PersistenceContext
    private EntityManager em;

    public void persist(InvoicePosition invoicePosition){
        em.persist(invoicePosition);
    }
}
