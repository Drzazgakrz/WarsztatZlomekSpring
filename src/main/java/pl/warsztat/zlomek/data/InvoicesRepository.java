package pl.warsztat.zlomek.data;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Invoice;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Repository
@Transactional
public class InvoicesRepository {

    @PersistenceContext
    private EntityManager em;

    private Logger log;

    @Autowired
    public InvoicesRepository(Logger log){
        this.log = log;
    }
    public void persist(Invoice invoice){
        em.persist(invoice);
    }
    public void update(Invoice invoice){
        em.merge(invoice);
    }

    public String generateInvoiceNumber(){
        LocalDate date = LocalDate.now();
        String regex = "%/"+date.getMonthValue()+"/"+date.getYear();
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(invoice) FROM Invoice invoice WHERE " +
                "invoice.invoiceNumber LIKE :regex",Long.class);
        query.setParameter("regex", regex);
        Long count = query.getSingleResult();
        log.info(Long.toString(count));
        return regex.replace("%", Long.toString(count+1));
    }

    public Invoice getInvoiceById(long id){
        try {
            TypedQuery<Invoice> query = em.createQuery("SELECT invoice FROM Invoice invoice WHERE invoice.id = :id",
                    Invoice.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }catch (NoResultException e){
            throw new ResourcesNotFoundException("Brak żądanej faktury");
        }


    }
}
