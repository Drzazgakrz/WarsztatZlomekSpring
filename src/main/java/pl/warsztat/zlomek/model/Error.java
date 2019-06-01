package pl.warsztat.zlomek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Error {
    private String reason;
    public Error(String reason) {
        this.reason = reason;
    }
}
