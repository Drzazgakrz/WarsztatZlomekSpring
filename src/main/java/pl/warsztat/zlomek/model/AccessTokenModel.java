package pl.warsztat.zlomek.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenModel {
    protected String accessToken;

    public AccessTokenModel(String accessToken) {
        this.accessToken = accessToken;
    }
}
