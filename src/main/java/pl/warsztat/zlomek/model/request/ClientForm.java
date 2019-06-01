package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientForm {
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phoneNumber;
    protected String cityName;
    protected String streetName;
    protected String buildNum;
    protected String aptNum;
    protected String zipCode;
    private String password;
    private String confirmPassword;
    private String accessToken;
}
