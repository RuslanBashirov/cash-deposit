package org.bashirov.cashdeposit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "phones", schema = "public")
public class Phones {
    @Id
    @SequenceGenerator(name = "phones_id_increment_seq", sequenceName = "phones_id_increment_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phones_id_increment_seq")
    private long id;

    @NotEmpty(message = "Field value shouldn't be empty")
    @Pattern(regexp="(^[0-9]+$)", message = "Field value is incorrect")
    @Column(unique = true)
    private String value;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Users users;

    public Phones(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Phones{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
