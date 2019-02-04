package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserNotFoundException extends RuntimeException {
    private String reason;

    public UserNotFoundException(String reason) {
        this.reason = reason;
    }
}
