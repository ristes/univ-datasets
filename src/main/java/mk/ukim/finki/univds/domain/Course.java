package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;

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

    public  static final String RDF_INSTANCE = "http://univ/Course";

    private static long idSequence = 0;

    private String year;

    @ManyToOne
    private User professor;

    @ManyToOne
    private Subject subject;

    @ManyToMany
    @JoinTable
    private Set<User> students = new HashSet<>();

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

}
