package pl.warsztat.zlomek.model.response;

import lombok.Getter;

import java.util.List;

@Getter
public class CompanyListResponse {
    private String accessToken;
    private List<CompanyResponse> companies;

    public CompanyListResponse(String accessToken, List<CompanyResponse> companies) {
        this.accessToken = accessToken;
        this.companies = companies;
    }
}
