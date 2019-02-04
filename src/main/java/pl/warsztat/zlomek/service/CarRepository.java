package pl.warsztat.zlomek.service;

import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Car;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CarRepository {

    @PersistenceContext
    private EntityManager em;

    public Car getCarByVin(String vin){
        try{
            TypedQuery<Car> query = em.createQuery("SELECT car from Car car WHERE car.vin=:vin", Car.class);
            query.setParameter("vin", vin);
            return query.getSingleResult();
        }catch (Exception e){}
        throw new ResourcesNotFoundException();
    }

    public void persistCar(Car car){
        em.persist(car);
    }

    public void updateCar(Car car){
        em.merge(car);
    }
}
