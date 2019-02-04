package pl.warsztat.zlomek.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Error {
    private String reason;

    private String[] reasons;
    public Error(String reason) {
        this.reason = reason;
    }

    public Error(String[] reasons){
        this.reasons = reasons;
    }
}
