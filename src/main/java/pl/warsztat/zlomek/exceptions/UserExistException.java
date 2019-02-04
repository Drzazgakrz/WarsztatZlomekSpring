package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserExistException extends RuntimeException {
    public String reason;

    public UserExistException(String reason) {
        this.reason = reason;
    }
}
