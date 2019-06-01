package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class RemoveClientFromCompanyRequest extends AccessTokenModel {
    private long companyId;
}
