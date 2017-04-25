package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
public class StudyProgram extends BaseEntity {

  public static final String RDF_INSTANCE = "http://univ/StudyProgram";

  private static long idSequence = 0;

  private String name;

  @ManyToOne
  private Faculty faculty;

  public static synchronized Long nextId() {
    idSequence++;
    return idSequence;
  }

  @Override
  public String getInstanceIRI() {
    if (id == null) {
      throw new RuntimeException("Cannot retrieve "
              + getClass().getCanonicalName()
              + " as semantic instance when id is null.");
    }
    return RDF_INSTANCE + "/" + getId();
  }
}
