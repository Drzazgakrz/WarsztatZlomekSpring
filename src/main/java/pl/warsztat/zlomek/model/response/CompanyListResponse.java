package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import pl.warsztat.zlomek.model.db.EmployeeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class CompanyListResponse {
    private String accessToken;
    private List<CompanyResponse> companies;

    public CompanyListResponse(String accessToken, List<CompanyResponse> companies) {
        this.accessToken = accessToken;
        this.companies = companies;
    }
}
