package pl.warsztat.zlomek.exceptions;

import lombok.Getter;

@Getter
public class ResourcesExistException extends RuntimeException {
    private String reason;
    private String accessToken;

    public ResourcesExistException(String reason, String accessToken) {
        this.reason = reason;
        this.accessToken = accessToken;
    }

    public ResourcesExistException(String reason) {
        this.reason = reason;
    }
}
