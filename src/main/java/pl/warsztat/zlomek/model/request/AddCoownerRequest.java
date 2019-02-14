package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class AddCoownerRequest extends AccessTokenModel {
    private long carId;
    private String[] newCoowners;
}
