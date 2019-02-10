package pl.warsztat.zlomek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AccessTokenModel {
    protected String accessToken;

    public AccessTokenModel(String accessToken) {
        this.accessToken = accessToken;
    }
}
