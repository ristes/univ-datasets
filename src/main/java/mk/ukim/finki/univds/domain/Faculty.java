package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
public class Faculty extends BaseEntity {

    public static final String RDF_INSTANCE = "http://univ/Faculty";

    private static long idSequence = 0;

    private String name;

    private String networkAddress;

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
