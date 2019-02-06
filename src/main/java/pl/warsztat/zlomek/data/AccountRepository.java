package pl.warsztat.zlomek.data;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;

@Repository
@Transactional
public  abstract class AccountRepository <Type extends Account>{
    @PersistenceContext
    protected EntityManager em;

    public abstract  String generateToken(Type account);

    public abstract Account findByToken(String accessToken);

    public void update(Type account) {
        em.merge(account);
    }

    public void insert(Type account) {
        em.persist(account);
    }

    protected String createToken(Type user){
        String token = "";
        try {
            Algorithm algorithm = Algorithm.HMAC256("secret");
            do{
                token = JWT.create()
                        .withIssuer(user.getEmail() + (new Date()).getTime())
                        .sign(algorithm);
            }while (findByToken(token)!=null);
        }catch (ResourcesNotFoundException e){}
        return token;
    }
}
