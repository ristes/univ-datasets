package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
@Namespace("http://univ#")
public class Course extends BaseEntity {

    private String year;

    @ManyToOne
    @RdfProperty("http://univ#has_professor")
    private User professor;

    @ManyToOne
    private Subject subject;

    @ManyToMany
    List<User> students =new ArrayList<>();

}
