package ex3.inheritance_table_per_class;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DiscriminatorValue(value = "M")
public class Movie extends Item {

    private String director;

    private String actor;

}
