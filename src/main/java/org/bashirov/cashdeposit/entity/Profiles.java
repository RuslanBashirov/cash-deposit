package org.bashirov.cashdeposit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "profiles", schema = "public")
public class Profiles {
    @Id
    @SequenceGenerator(name = "profiles_id_increment_seq", sequenceName = "profiles_id_increment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profiles_id_increment_seq")
    private long id;

    @NotNull(message = "Field cash shouldn't be empty")
    @Digits(message = "Field cash is incorrect", integer = 18, fraction = 0)
    @Min(value=0, message = "Field cash shouldn't be negative")
    private long cash;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users users;

    public Profiles(long cash) {
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "Profiles{" +
                "id=" + id +
                ", cash=" + cash +
                '}';
    }
}
