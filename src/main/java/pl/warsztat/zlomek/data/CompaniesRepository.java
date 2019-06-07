package pl.warsztat.zlomek.data;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesExistException;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Company;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class CompaniesRepository {
    @PersistenceContext
    private EntityManager em;

    public void createCompany(Company company) {
        try {
            em.persist(company);
        } catch (Exception e) {
            throw new ResourcesExistException("Firma o podanej nazwie lub numerze nip istnieje");
        }
    }

    public Company getCompanyByNip(String nip) {
        try {
            TypedQuery<Company> query = em.createQuery("SELECT company FROM Company company WHERE company.nip = :nip",
                    Company.class);
            query.setParameter("nip", nip);
            return query.getSingleResult();

        } catch (Exception e) {
        }
        throw new ResourcesNotFoundException("Firma o podanym numerze nip nie istnieje");
    }

    public Company getCompanyId(long id) {
        try {
            TypedQuery<Company> query = em.createQuery("SELECT company FROM Company company WHERE company.id = :id",
                    Company.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new ResourcesNotFoundException("Brak firmy o podanym id");
        }
    }

    public Company getCompanyName(String name){
        try {
            TypedQuery<Company> query = em.createQuery("SELECT company FROM Company company " +
                    "WHERE company.companyName=:name", Company.class);
            query.setParameter("name", name);
            return query.getSingleResult();
        }catch (Exception e){}
        return null;
    }

    public void updateCompany(Company company) {
        em.merge(company);
    }

    public List<Company> getCompanies() {
        TypedQuery<Company> query = em.createQuery("SELECT company FROM Company company ", Company.class);
        return query.getResultList();
    }
}
