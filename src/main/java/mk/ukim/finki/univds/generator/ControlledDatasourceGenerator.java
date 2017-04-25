package mk.ukim.finki.univds.generator;

import mk.ukim.finki.univds.domain.*;
import mk.ukim.finki.univds.generator.factories.CourseFactory;
import mk.ukim.finki.univds.generator.factories.GradeFactory;
import mk.ukim.finki.univds.generator.factories.UserFactory;
import mk.ukim.finki.univds.repository.CourseRepository;
import mk.ukim.finki.univds.repository.GradeRepository;
import mk.ukim.finki.univds.repository.UserRepository;
import mk.ukim.finki.univds.repository.impl.StudentRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Generates the default data set as described in section 5.
 */
@Service
public class ControlledDatasourceGenerator {

    @Autowired
    private StudentRepositoryImpl studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GradeRepository gradeRepository;

    public void generateGradeCourseRelation(User professor, Subject subject, double numberOfGrades) {

        // ----> generate courses
        Course course = CourseFactory.make();
        course.setProfessor(professor);
        course.setSubject(subject);
        courseRepository.save(course);

        StudyProgram studyProgram = subject.getStudyPrograms().stream().findFirst().get();

        for (int i = 0; i < numberOfGrades; i++) {
            // generate students
            User student = UserFactory.make(UserFactory.STUDENT_TYPE);
            student.setStudyProgram(studyProgram);
            studentRepository.save(student);

            // add students to courses
            course.getStudents().add(student);

            // ----> generate grades
            Grade grade = GradeFactory.make();
            grade.setCourse(course);
            grade.setStudent(student);
            gradeRepository.save(grade);
        }
        courseRepository.save(course);
    }
}
