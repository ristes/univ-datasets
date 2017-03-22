package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
@Namespace("http://univ#")
public class User extends BaseEntity {

    @Column(length = 31)
    private String DTYPE;

    private String name;

    private String email;

    private String phone;

    @ManyToOne
    @RdfProperty("http://univ#works_at")
    private Faculty faculty;

    @ManyToOne
    @RdfProperty("http://univ#enrolled_at")
    private StudyProgram studyProgram;

}
