package mk.ukim.finki.univds.service;

import mk.ukim.finki.univds.domain.*;

import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface QueryService {

  List<BaseEntity> listByNameContains(String namePart);


  interface Courses {

    List<Course> listAllActiveCourses();

    List<Course> listCoursesByYear(int year);

    List<User> listCourseStudents(String courseIri);

    List<User> listStudentsWithoutGrade(String courseIri);

    List<User> getStudentsWithGradeOnCourseGreaterThen(String courseIri, int grade);

    User getCourseProfessor(String courseIri);

    Double getCourseAverage(String courseIri);

  }

  interface Subjects {

    List<User> listAllSubjects();

    List<User> listSubjectProfessors(String subjectIri);

    List<Course> listSubjectCourses(String courseIri);

    Double getSubjectAverage(String subjectIri);


  }

  interface Users {

    User describeUser(String iri);

    List<User> listUsersByEmail(String email);

    List<User> listUsersByPhone(String phone);

    List<User> listMyColleagues(); // students and professors connected somehow with me
  }

  interface Students extends Users {

    List<User> listAllStudents();

    List<User> getStudentsWithAverageGreaterThen(int grade);

    List<Subject> getStudentSubjects(String studentIri);

    List<Course> getStudentCourses(String studentIri);

    List<Grade> getStudentGrades(String studentIri);

    Double getStudentAverage(String studentIri);

  }

  interface Professors extends Users {

    List<User> listAllProfessors();

    List<User> listStudentsByProfessor(String professorIri);

    List<Course> listProfessorCourses(String professorIri);

    List<Subject> listProfessorSubjects(String professorIri);


  }


}
