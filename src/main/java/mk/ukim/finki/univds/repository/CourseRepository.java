package mk.ukim.finki.univds.repository;

import mk.ukim.finki.univds.domain.Course;
import mk.ukim.finki.univds.domain.User;

import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public interface CourseRepository {

  void save(Course course);

   void saveStudentsForCourse(Course course, List<User> students);
}
