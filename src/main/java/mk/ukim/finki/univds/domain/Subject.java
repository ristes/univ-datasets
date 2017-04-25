package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;

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
public class Subject extends BaseEntity{

    public static final String RDF_INSTANCE = "http://univ/Subject";

    private static long idSequence = 0;

    private String description;

    private String name;

    @ManyToMany
    @JoinTable
    private Set<StudyProgram> studyPrograms  = new HashSet<>();

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
