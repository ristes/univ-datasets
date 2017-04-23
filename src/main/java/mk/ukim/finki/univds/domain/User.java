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
public class User extends BaseEntity {

    @Column(length = 31)
    private String DTYPE;

    private String name;

    private String email;

    private String phone;

    @ManyToOne
    private Faculty faculty;

    @ManyToOne

    private StudyProgram studyProgram;

    public static Long nextStudentId() {

    }


    public static Long nextStaffId() {

    }


    public static void setBaseStudentId(Long base) {

    }

    public static void setBaseStaffId(Long base) {

    }
}
