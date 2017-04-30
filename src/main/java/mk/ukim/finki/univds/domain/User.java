package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;

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

    public static final String RDF_INSTANCE = "http://univ/User";

    private static long idSequence = 50;

    @Column(length = 31)
    private String DTYPE;

    private String name;

    private String email;

    private String phone;

    @ManyToOne
    private Faculty faculty;

    @ManyToOne

    private StudyProgram studyProgram;

    @Override
    public String getInstanceIRI() {
        if (id == null) {
            throw new RuntimeException("Cannot retrieve "
                    + getClass().getCanonicalName()
                    + " as semantic instance when id is null.");
        }
        return RDF_INSTANCE + "/" + getId();
    }

    public static synchronized Long nextId() {
        idSequence++;
        return idSequence;
    }

//
//    public static void setBaseStudentId(Long base) {
//
//    }
//
//    public static void setBaseStaffId(Long base) {
//
//    }
}
