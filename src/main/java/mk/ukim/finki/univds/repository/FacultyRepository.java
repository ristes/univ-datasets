package mk.ukim.finki.univds.repository;

import mk.ukim.finki.univds.domain.Faculty;

import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public interface FacultyRepository {
  List<Faculty> findAll();


  void save(List<Faculty> faculties);

  void save(Faculty faculties);
}
