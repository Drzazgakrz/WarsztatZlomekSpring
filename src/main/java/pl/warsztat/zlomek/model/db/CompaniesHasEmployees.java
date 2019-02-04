package pl.warsztat.zlomek.model.db;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "companies_has_employees")
@NoArgsConstructor
@Getter
@Setter
public class CompaniesHasEmployees {

    @EmbeddedId
    private CompaniesHasEmployeesId id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    private Company company;

    private EmployeeStatus status;

    public CompaniesHasEmployees(Client client, Company company) {
        this.id = new CompaniesHasEmployeesId(company.getId(), client.getClientId());
        this.client = client;
        this.company = company;
        this.status = EmployeeStatus.CURRENT_EMPLOYER;
    }

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    private static class CompaniesHasEmployeesId implements Serializable {
        @Column(name = "company_pk")
        private long companyId;

        @Column(name = "client_pk")
        private long clientId;

        public CompaniesHasEmployeesId(long companyId, long clientId) {
            this.companyId = companyId;
            this.clientId = clientId;
        }
    }
}
