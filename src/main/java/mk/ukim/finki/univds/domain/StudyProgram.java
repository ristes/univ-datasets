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

  public static String CLASS = "http://univ/StudyProgram";
  public static String INSTANCE = "http://univ/StudyProgram/%d";
  public static String _NAME = "http://www.w3.org/2000/01/rdf-schema#label";
  public static String _FACULTY = "http://univ/faculty";
  private static long idSequence = 0;
  private String name;

  @ManyToOne
  private Faculty faculty;

  public static synchronized Long nextId() {
    idSequence++;
    return idSequence;
  }
}
