package pl.warsztat.zlomek.model.response;

import lombok.Getter;
import lombok.Setter;
import pl.warsztat.zlomek.model.db.Client;
import pl.warsztat.zlomek.model.request.ClientForm;
@Getter
@Setter
public class ClientDataResponse extends ClientForm {
    private long id;
    private String accessToken;

    public ClientDataResponse(Client client, String accessToken){
        this.id = client.getClientId();
        this.aptNum = client.getAptNum();
        this.buildNum = client.getAptNum();
        this.cityName = client.getCityName();
        this.email = client.getEmail();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.phoneNumber = client.getPhoneNumber();
        this.streetName = client.getStreetName();
        this.zipCode = client.getZipCode();
        this.accessToken = accessToken;
    }
}
