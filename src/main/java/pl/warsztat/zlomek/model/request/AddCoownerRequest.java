package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

import java.util.List;

@Getter
public class AddCoownerRequest extends AccessTokenModel {
    private long carId;
    private String coownerUsername;
}
