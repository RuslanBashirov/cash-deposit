package org.bashirov.cashdeposit.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class Users {

    @Id
    @SequenceGenerator(name = "users_id_increment_seq", sequenceName = "users_id_increment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_increment_seq")
    private long id;

    @NotEmpty(message = "Field name shouldn't be empty")
    private String name;

    @NotNull(message = "Field age shouldn't be empty")
    @Digits(message = "Field age is incorrect", integer = 3, fraction = 0)
    @Min(value=0, message = "Field age shouldn't be negative")
    @Max(value=150, message = "Field age is too big")
    private int age;

    @NotEmpty(message = "Field age shouldn't be empty")
    @Email(message = "Field email should have email form")
    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private Profiles profiles;

    @JsonManagedReference
    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Phones> phones;

    public Users(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", profile=" + profiles +
                ", phones=" + phones +
                '}';
    }
}
