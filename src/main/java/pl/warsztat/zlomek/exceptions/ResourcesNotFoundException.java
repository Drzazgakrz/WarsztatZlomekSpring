package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourcesNotFoundException extends RuntimeException {
    private String reason;
    private String accessToken;

    public ResourcesNotFoundException(String reason, String accessToken) {
        this.reason = reason;
        this.accessToken = accessToken;
    }

    public ResourcesNotFoundException(String reason) {
        this.reason = reason;
    }
}
