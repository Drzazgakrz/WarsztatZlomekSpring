package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class AccessToken implements Serializable {
    @Column(name = "access_token")
    protected String accessToken;

    protected LocalDateTime expiration;

    public AccessToken(String accessToken, LocalDateTime expiration) {
        this.accessToken = accessToken;
        this.expiration = expiration;
    }
}
