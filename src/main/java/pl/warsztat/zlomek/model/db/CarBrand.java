package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class CarBrand implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "brand_name")
    @Pattern(regexp = "[A-Z]{1}+[a-z]{1,}")
    private String brandName;

    public CarBrand(String name){
        this.brandName = name;
    }
}
