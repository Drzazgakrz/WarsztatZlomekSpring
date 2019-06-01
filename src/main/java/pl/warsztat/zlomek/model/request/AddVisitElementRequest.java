package pl.warsztat.zlomek.model.request;

import lombok.Getter;
import pl.warsztat.zlomek.model.AccessTokenModel;

@Getter
public class AddVisitElementRequest extends AccessTokenModel {
    protected String name;
    protected int tax;
}
