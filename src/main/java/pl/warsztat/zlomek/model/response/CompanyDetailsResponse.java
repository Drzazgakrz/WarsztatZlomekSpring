package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.warsztat.zlomek.model.db.CompanyModel;

@Getter
@Setter
public class CompanyDetailsResponse extends CompanyResponse {
    private String accessToken;
    public CompanyDetailsResponse(CompanyModel companyModel, String accessToken) {
        super(companyModel);
        this.accessToken = accessToken;
    }
}
