package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Setter
@Getter
@Namespace("http://univ#")
public class Subject extends BaseEntity{

    private String description;

    @RdfProperty("http://www.w3.org/2000/01/rdf-schema#label")
    private String name;

    @ManyToMany
    @JoinTable
    private Set<StudyProgram> studyPrograms  = new HashSet<>();

}
