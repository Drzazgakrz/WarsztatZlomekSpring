package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.Setter;
import pl.warsztat.zlomek.model.AccessTokenModel;

import java.util.List;

@Getter
@Setter
public class VerifyOwnershipRequest extends AccessTokenModel {
    private long carId;
    private List<ClientForm> verified;
    private List<ClientForm> netVerified;
}
