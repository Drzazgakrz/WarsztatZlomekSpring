package pl.warsztat.zlomek.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldsNotCorrect extends RuntimeException{
    private String[] fields;

    public FieldsNotCorrect(String[] fieldName) {
        this.fields = fieldName;
    }
}
