package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import java.util.List;

@Getter
public class CompaniesList {
    private List<CompanyResponse> companies;

    public CompaniesList(List<CompanyResponse> companies){
        this.companies = companies;
    }
}
