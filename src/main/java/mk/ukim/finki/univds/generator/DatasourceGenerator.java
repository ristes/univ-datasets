package mk.ukim.finki.univds.generator;

import mk.ukim.finki.univds.domain.*;
import mk.ukim.finki.univds.generator.factories.*;
import mk.ukim.finki.univds.repository.*;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Component
public class DatasourceGenerator {

  private final Logger log = LoggerFactory.getLogger(DatasourceGenerator.class);

  @Autowired
  private FacultyRepository facultyRepository;

  @Autowired
  private StudyprogramRepository studyProgramRepository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private GradeRepository gradeRepository;

  /**
   * Generates data and saves it to mysql database. parameters are used to calculate how many
   * entities will be generated in the data model. <br /><br />
   * <p>
   * The generation strategy is creating entities in the following order:
   * <ol>
   * <li>{@link mk.ukim.finki.univds.domain.Faculty}</li>
   * <li>{@link mk.ukim.finki.univds.domain.StudyProgram}</li>
   * <li>{@link mk.ukim.finki.univds.domain.User} - Professor (DTYPE= {@link UserFactory#PROFESSOR_TYPE})</li>
   * <li>{@link mk.ukim.finki.univds.domain.Subject}</li>
   * <li>{@link mk.ukim.finki.univds.domain.Course}</li>
   * <li>{@link mk.ukim.finki.univds.domain.User} - Professor (DTYPE= {@link UserFactory#STUDENT_TYPE})</li>
   * <li>{@link mk.ukim.finki.univds.domain.Grade}</li>
   * </ol>
   * <p>
   * Number of entities increases if entity type is created later in the process (e.g. {@link Faculty} will have
   * least amount of entities because it is created first. {@link Grade} will have most entities because it is created last)
   * <br /><br />
   * <p>
   * For each entity type the number of enries is calculated like this: <br />
   * <code>
   * base * (multiplier^zero based generation order index) * factor
   * </code>
   * <br /><br />
   * meaning each entity type will have the following number of entities:
   * <p>
   * <ol>
   * <li>Num entities for {@link Faculty} =  base * (multiplier^0) * factor</li>
   * <li>Num entities for {@link StudyprogramRepository} =  base * (multiplier^1) * factor</li>
   * <li>Num entities for {@link User} - Professor (DTYPE= {@link UserFactory#PROFESSOR_TYPE}) =  base * (multiplier^2) * factor</li>
   * <li>Num entities for {@link Subject} =  base * (multiplier^3) * factor</li>
   * <li>Num entities for {@link Course} =  base * (multiplier^4) * factor</li>
   * <li>Num entities for {@link User} - Student (DTYPE= {@link UserFactory#STUDENT_TYPE}) =  base * (multiplier^5) * factor</li>
   * <li>Num entities for {@link Grade} =  base * (multiplier^6) * factor</li>
   * </ol>
   *
   * @param base       the base generation parameter.
   * @param multiplier the multiplier generation parameter.
   * @param factor     the factor generation parameter.
   */
  //    build procedure:
//    - Fakultet            1            1 * factor       base * (multiplyer^0) * factor
//    - StudyProgram		10           10 * factor      base * (multiplyer^1) * factor
//    - subject?			1000         100 * factor     base * (multiplyer^2) * factor
//    - profesor + student  100 + 100000 1000 * factor    base * (multiplyer^3) * factor
//    - course 			    10000        10000 * factor   base * (multiplyer^4) * factor
//    - grade				1000000      100000 * factor  base * (multiplyer^5) * factor
  public void generateData(double base, double multiplier, double factor) {
    Integer multiplierFactor = 0;

    //generate faculties
    int numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<Faculty> faculties = FacultyFactory.make(numEntities);
    log.info("{} number of Faculties created. Starting to save the collection", numEntities);
    facultyRepository.save(faculties);
    log.info("Collection is saved.", numEntities);

    // generate StudyPrograms
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<StudyProgram> StudyPrograms = StudyprogramFactory.make(numEntities);
    log.info("{} number of StudyProgram created. Now pairing with Faculty", numEntities);
    pairStudyProgramAndFaculty(StudyPrograms, faculties);

    // generate professors
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<User> professors = UserFactory.make(numEntities, UserFactory.PROFESSOR_TYPE);
    log.info("{} number of Professors created. Now pairing with Faculty", numEntities);
    pairProfessorsAndFaculties(professors, faculties);

    // generate subjects
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<Subject> subjects = SubjectFactory.make(numEntities);
    log.info("{} number of Subjects created. Now pairing with StudyProgram", numEntities);
    pairSubjectAndStudyProgram(subjects, StudyPrograms);

    // generate courses
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<Course> courses = CourseFactory.make(numEntities);
    log.info("{} number of Courses created. Now pairing with Professors and Subjects", numEntities);
    pairCoursesWithProfessorsAndSubjects(courses, professors, subjects);

    // generate students
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++;
    List<User> students = UserFactory.make(numEntities, UserFactory.STUDENT_TYPE);
    log.info("{} number of Students created. Now pairing with Study programs", numEntities);
    pairStudentsWithStudyProgram(students, StudyPrograms);

    // add students to courses
    log.info("Students now pairing with Courses");
    pairStudentsWithCourses(students, courses);

    // generate grades
    numEntities = nextEntitiesCount(base, multiplier, factor, multiplierFactor);
    multiplierFactor++; // don't need to be done here, but lets be consistent.
    List<Grade> grades = GradeFactory.make(numEntities);
    log.info("{} number of Grades created. Now pairing with Students from courses", numEntities);
    pairGradesWithStudentsFromCourses(grades, courses);

    log.info("thats all folks. was about time.");
  }

  private void pairStudyProgramAndFaculty(List<StudyProgram> studyPrograms, List<Faculty> faculties) {
    // every study program must belong to a faculty
    for (StudyProgram studyProgram : studyPrograms) {
      // pick random faculty
      int randomFacultyIndex = RandomUtils.nextInt(0, faculties.size());
      // retrieve the object
      Faculty faculty = faculties.get(randomFacultyIndex);
      // pair them
      studyProgram.setFaculty(faculty);
      studyProgramRepository.save(studyProgram);
    }
    log.info("Pairing done. Saved {} entities.", studyPrograms.size());
  }

  private void pairSubjectAndStudyProgram(List<Subject> subjects, List<StudyProgram> StudyPrograms) {
    // relation is @ManyToMany, any subject can belong to multiple study programs
    for (Subject subject : subjects) {
      int numberStudyProgramsToPair = RandomUtils.nextInt(1, StudyPrograms.size());
      for (int i = 0; i < numberStudyProgramsToPair; i++) {
        int randomStudyProgramIndex = RandomUtils.nextInt(0, StudyPrograms.size());
        StudyProgram StudyProgram = StudyPrograms.get(randomStudyProgramIndex);
        subject.getStudyPrograms().add(StudyProgram);
      }
      subjectRepository.save(subject);
    }
    log.info("Pairing done. Saved {} entities.", subjects.size());
  }

  private void pairProfessorsAndFaculties(List<User> professors, List<Faculty> faculties) {
    // every Professor must belong to a faculty
    for (User professor : professors) {
      // pick random faculty
      int randomFacultyIndex = RandomUtils.nextInt(0, faculties.size());
      // retrieve the object
      Faculty faculty = faculties.get(randomFacultyIndex);
      // pair them
      professor.setFaculty(faculty);
      userRepository.save(professor);
    }
    log.info("Pairing done. Saved {} entities", professors.size());
  }

  private void pairCoursesWithProfessorsAndSubjects(List<Course> courses, List<User> professors, List<Subject> subjects) {
    // every course is paired with a subject. Every course is paired with professor. Not every subject has a course.
    // professor and subject pairs must be distinct

    // helper map to keep track what subjects are already paired with each professor
    Map<User, Set<Subject>> pairedProfessorsAndSubjects = new HashMap<>();
    int coursesFilled = 0;

    // First iterate over all professors and associate it with one subject.
    // This way all professors will have at least one course.
    // pairedProfessorsAndSubjects map will keep track of
    // what subject has been assigned to each professor so the same pair don't happen again
    for (User professor : professors) {

      int randomSubjectIndex = RandomUtils.nextInt(0, subjects.size());
      Subject subject = subjects.get(randomSubjectIndex);

      Set<Subject> subjectsForProfessor = new HashSet<>();
      subjectsForProfessor.add(subject);
      pairedProfessorsAndSubjects.put(professor, subjectsForProfessor);

      Course course = courses.get(coursesFilled);
      course.setProfessor(professor);
      course.setSubject(subject);
      coursesFilled++;
      courseRepository.save(course);
    }

    // For the rest of the courses, iterate over random professors and assign subject
    // that is not already assigned to it (pairedProfessorsAndSubjects map will help for that).
    // This is ok because of the generation strategy, courses will always be more in number than professors.
    for (int i = coursesFilled; i < courses.size(); i++) {
      int randomProfessorIndex = RandomUtils.nextInt(0, professors.size());
      User professor = professors.get(randomProfessorIndex);
      Set<Subject> pairedSubjectsForProfessor = pairedProfessorsAndSubjects.get(professor);

      boolean subjectAlreadyPairedWithProfessor = true;
      Subject subject = null;
      do {
        int randomSubjectIndex = RandomUtils.nextInt(0, subjects.size());
        subject = subjects.get(randomSubjectIndex);
        if (!pairedSubjectsForProfessor.contains(subject)) {
          pairedSubjectsForProfessor.add(subject);
          subjectAlreadyPairedWithProfessor = false;
        }
      } while (subjectAlreadyPairedWithProfessor);

      // subject that is not paired with professor picked. update the course.
      Course course = courses.get(i);
      course.setProfessor(professor);
      course.setSubject(subject);
      courseRepository.save(course);
    }
    log.info("Pairing done. Saved {} entities", courses.size());
  }

  private void pairStudentsWithStudyProgram(List<User> students, List<StudyProgram> StudyPrograms) {
    // every student must belong to a study program
    for (User student : students) {
      // pick random study program
      int randomStudyProgramIndex = RandomUtils.nextInt(0, StudyPrograms.size());
      // retrieve the object
      StudyProgram StudyProgram = StudyPrograms.get(randomStudyProgramIndex);
      // pair them
      student.setStudyProgram(StudyProgram);
      userRepository.save(student);
    }
    log.info("Pairing done. Saved {} entities", students.size());
  }

  private Set<Integer> generateSetWithSize(int size, int upperLimit) {

    Set<Integer> result = new HashSet<>();

    while (result.size() < size) {
      int randomInteger = RandomUtils.nextInt(0, upperLimit);
      result.add(randomInteger);
    }

    return result;
  }

  private void pairStudentsWithCourses(List<User> students, List<Course> courses) {

    // limit the students per course to maximum of 300, because it takes too much time to generate
    int studentsPerCourseLimit = 300;
    if (students.size() < studentsPerCourseLimit) {
      studentsPerCourseLimit = students.size();
    }
    // assure that every course will have at least one student,
    // otherwise courses without students don't make any sense and are false data.
    for (Course course : courses) {
      int numberStudentsAttendingTheCourse = RandomUtils.nextInt(1, studentsPerCourseLimit);
      Set<Integer> studentsIndexes = generateSetWithSize(numberStudentsAttendingTheCourse, students.size());
      for (Integer index : studentsIndexes) {
        User student = students.get(index);
        course.getStudents().add(student);
      }
      courseRepository.save(course);
    }
    log.info("Pairing done. Saved {} entities", courses.size());
  }

  private void pairGradesWithStudentsFromCourses(List<Grade> grades, List<Course> courses) {
    // every grade must have a student from the corresponding course.
    // there can't be more than one grade for student per course.
    Map<Course, Set<User>> gradeForStudentPerCourse = new HashMap<>();
    for (Grade grade : grades) {
      boolean pickedStudentAlreadyWithGrade = true;
      do {
        int randomCourseIndex = RandomUtils.nextInt(0, courses.size());
        Course course = courses.get(randomCourseIndex);

        if (!gradeForStudentPerCourse.containsKey(course)) {
          gradeForStudentPerCourse.put(course, new HashSet<>());
        }
        Set<User> studentsWithGradeForCourse = gradeForStudentPerCourse.get(course);

        int randomStudentFromCourse = RandomUtils.nextInt(0, course.getStudents().size());
        List<User> students = course.getStudents().stream().collect(Collectors.toList());
//                students.addAll(course.getStudents());
        User student = students.get(randomStudentFromCourse);
        if (!studentsWithGradeForCourse.contains(student)) {
          // update the grade properties
          grade.setCourse(course);
          grade.setStudent(student);

          // update the map that keeps track of grades student mappings for course
          studentsWithGradeForCourse.add(student);
          gradeForStudentPerCourse.put(course, studentsWithGradeForCourse);

          // set the repeat flag to false
          pickedStudentAlreadyWithGrade = false;
        }
      } while (pickedStudentAlreadyWithGrade);
      gradeRepository.save(grade);
    }
    log.info("Pairing done. Saved {} entities", grades.size());
  }

  private int nextEntitiesCount(double base, double multiplier, double factor, Integer multiplierFactor) {
    double result = base * Math.pow(multiplier, multiplierFactor) * factor;
    return (int) Math.ceil(result);
  }
}
