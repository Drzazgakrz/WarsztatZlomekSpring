package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public class VisitElement implements Serializable {

    @NotNull
    @Size(max = 255, min = 6)
    protected String name;

    @NotNull
    @Max(value = 25)
    @Min(value = 0)
    protected int tax;
}
