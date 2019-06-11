package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "client_token")
public class ClientToken extends AccessToken implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    public ClientToken(String accessToken, LocalDateTime expiration, Client client) {
        super(accessToken, expiration);
        this.client = client;
    }
}
