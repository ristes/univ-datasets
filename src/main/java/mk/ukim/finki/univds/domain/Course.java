package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
public class Course extends BaseEntity {

    private String year;

    @ManyToOne
    private User professor;

    @ManyToOne
    private Subject subject;

    @ManyToMany
    @JoinTable
    private Set<User> students = new HashSet<>();

}
