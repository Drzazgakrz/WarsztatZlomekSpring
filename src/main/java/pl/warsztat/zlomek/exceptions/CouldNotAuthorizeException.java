package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CouldNotAuthorizeException extends RuntimeException {
    private String reason;

    public CouldNotAuthorizeException(String reason) {
        this.reason = reason;
    }
}
