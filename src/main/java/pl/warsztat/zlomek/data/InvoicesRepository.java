package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
@Transactional
public class InvoicesRepository {

    @PersistenceContext
    private EntityManager em;
}
