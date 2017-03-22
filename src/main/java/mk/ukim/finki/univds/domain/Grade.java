package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
@Namespace("http://univ#")
public class Grade extends BaseEntity {

    private Integer grade;


    @ManyToOne
    @RdfProperty("http://univ#for_student")
    private User student;

    @ManyToOne
    @RdfProperty("http://univ#has_course")
    private Course course;
}
