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
public class StudyProgram extends BaseEntity {


    @RdfProperty("http://www.w3.org/2000/01/rdf-schema#label")
    private String name;

    @ManyToOne
    private Faculty faculty;

}
