package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.Setter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
@Setter
public class AddClientToCompanyRequest extends AccessTokenModel {
    private String companyName;
    private String username;
}
