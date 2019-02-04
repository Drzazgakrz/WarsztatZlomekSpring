package pl.warsztat.zlomek.model.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@NoArgsConstructor
public abstract class Account implements Serializable {
    @NotNull
    @Size(max = 30, min = 3)
    @Pattern(regexp = "[A-ZŹĄĘÓŁŻ]{1}+[a-z,ąęółńćźż]{2,}")
    @Column(name = "first_name")
    protected String firstName;

    @NotNull
    @Size(max = 30, min = 2)
    @Pattern(regexp = "[A-ZŹĄĘÓŁŻ]{1}+[a-z,ąęółńćźż]{2,}")
    @Column(name = "last_name")
    protected String lastName;

    @NotNull
    @Size(max=60, min = 60)
    protected String password;

    @Column(unique = true)
    @Pattern(regexp = "[A-Za-z0-9._-]{1,}+@+[a-z]{1,6}+.+[a-z]{2,3}")
    protected String email;

    @Column(name = "created_at")
    protected LocalDateTime createdAt;

    @Column(name = "last_logged_in")
    protected  LocalDateTime lastLoggedIn;

    public Account(String email, String firstname, String lastName, String password, LocalDateTime createdAt, LocalDateTime lastLoggedIn){
        this.email = email;
        this.firstName = firstname;
        this.lastName = lastName;
        this.password = new BCryptPasswordEncoder().encode(password);
        this.lastLoggedIn = lastLoggedIn;
        this.createdAt = createdAt;
    }
}
