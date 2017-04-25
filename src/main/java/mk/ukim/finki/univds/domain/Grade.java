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
public class Grade extends BaseEntity {

  public static final String RDF_INSTANCE = "http://univ/Grade";

  private static long idSequence = 0;

  private Integer grade;


  @ManyToOne
  private User student;

  @ManyToOne
  private Course course;

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
