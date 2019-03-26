package pl.warsztat.zlomek.data;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.warsztat.zlomek.exceptions.ResourcesNotFoundException;
import pl.warsztat.zlomek.model.db.*;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional
public class ClientRepository extends AccountRepository<Client>{
    public Client signIn(String username, String password){
        try {
            TypedQuery<Client> getClient = em.createQuery("SELECT client FROM Client client "+
                    "WHERE client.email = :username",Client.class);
            getClient.setParameter("username", username);
            Client client = getClient.getSingleResult();
            if(new BCryptPasswordEncoder().matches(password, client.getPassword()))
                return client;
        }catch (Exception e){
            e.printStackTrace();
        }
        throw new ResourcesNotFoundException("Brak klienta o podanych danych");
    }

    public Client findClientByUsername(String username){
        try {
            TypedQuery<Client> getClient = em.createQuery("select client from Client client "+
                    "where client.email = :username ",Client.class);
            getClient.setParameter("username", username);
            return getClient.getSingleResult();
        }catch (Exception e){
            e.printStackTrace();
            throw new ResourcesNotFoundException("Brak klienta o podanym adresie e-mail");
        }
    }

    @Override
    public Client findByToken(String accessToken) {
        try {
            TypedQuery<ClientToken> query = em.createQuery("SELECT clientToken FROM ClientToken clientToken " +
                    "WHERE clientToken.accessToken = :accessToken", ClientToken.class);
            query.setParameter("accessToken", accessToken);
            AccessToken token =  query.getSingleResult();
            if(token.getExpiration().isAfter(LocalDateTime.now())){
                token.setExpiration(LocalDateTime.now().plusMinutes(20));
                em.merge(token);
                return ((ClientToken) token).getClient();
            }
        }catch (Exception e){
        }
        throw new ResourcesNotFoundException("Klient o podanym tokenie nie istnieje bądź token wygasł");
    }

    @Override
    public String generateToken(Client account) {
        String token = createToken(account);
        ClientToken clientToken = new ClientToken(token, LocalDateTime.now().plusMinutes(20), account);
        em.persist(clientToken);
        account.getAccessToken().add(clientToken);
        update(account);
        return token;
    }

    public void signOut(String accessToken){
        try {
            TypedQuery<ClientToken> query = em.createQuery("SELECT clientToken FROM ClientToken clientToken WHERE clientToken.accessToken = :accessToken", ClientToken.class);
            query.setParameter("accessToken", accessToken);
            ClientToken token = query.getSingleResult();
            token.setExpiration(LocalDateTime.now());
            em.merge(token);
        }catch (Exception e){}
    }

    public Client getClientById(long id){
        try {
            TypedQuery<Client> query = em.createQuery("SELECT client FROM Client client WHERE client.clientId = :id", Client.class);
            query.setParameter("id", id);
            return query.getSingleResult();
        }catch (Exception e){
            throw new ResourcesNotFoundException("Taki klient nie istnieje");
        }
    }

    public List<Client> getClientsByStatus(ClientStatus status){
        TypedQuery<Client> query = em.createQuery("SELECT client FROM Client client WHERE status = :status", Client.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
