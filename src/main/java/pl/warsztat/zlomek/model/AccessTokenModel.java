package pl.warsztat.zlomek.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenModel {
    protected String accessToken;

    public AccessTokenModel(String accessToken) {
        this.accessToken = accessToken;
    }
}
