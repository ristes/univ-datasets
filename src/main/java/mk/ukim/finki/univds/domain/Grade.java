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

  private static Long idSequence;

  private Integer grade;


  @ManyToOne
  private User student;

  @ManyToOne
  private Course course;

  public static synchronized Long nextId() {
    idSequence++;
    return idSequence;

  }
}
