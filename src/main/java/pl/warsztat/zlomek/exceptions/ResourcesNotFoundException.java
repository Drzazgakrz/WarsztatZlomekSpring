package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourcesNotFoundException extends RuntimeException {
    private String reason;

    public ResourcesNotFoundException(String reason) {
        this.reason = reason;
    }
}
