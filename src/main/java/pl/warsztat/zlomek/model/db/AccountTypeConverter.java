package pl.warsztat.zlomek.model.db;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountTypeConverter implements Converter<String, AccountType> {
    @Override
    public AccountType convert(String source) {
        return AccountType.valueOf(source);
    }
}
