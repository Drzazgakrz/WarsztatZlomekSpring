package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldsNotCorrect extends RuntimeException{
    private String[] fields;
    private String accessToken;

    public FieldsNotCorrect(String[] fields, String accessToken) {
        this.fields = fields;
        this.accessToken = accessToken;
    }

    public FieldsNotCorrect(String[] fieldName) {
        this.fields = fieldName;
    }
}
