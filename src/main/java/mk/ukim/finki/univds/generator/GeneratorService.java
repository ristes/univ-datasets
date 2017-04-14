package mk.ukim.finki.univds.generator;

import mk.ukim.finki.univds.domain.Faculty;
import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.domain.Subject;
import mk.ukim.finki.univds.domain.User;
import mk.ukim.finki.univds.generator.factories.FacultyFactory;
import mk.ukim.finki.univds.generator.factories.StudyprogramFactory;
import mk.ukim.finki.univds.generator.factories.SubjectFactory;
import mk.ukim.finki.univds.generator.factories.UserFactory;
import mk.ukim.finki.univds.repository.FacultyRepository;
import mk.ukim.finki.univds.repository.StudyprogramRepository;
import mk.ukim.finki.univds.repository.SubjectRepository;
import mk.ukim.finki.univds.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service that generates data.
 */
@Service
public class GeneratorService {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(GeneratorService.class);

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudyprogramRepository studyProgramRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ControlledDatasourceGenerator datasourceGenerator;

    public void generate(int generatorIterations) {

        List<Faculty> faculties = facultyRepository.findAll();

        // there should always be 1 faculty.
        if (faculties.isEmpty()) {
            Faculty faculty = FacultyFactory.make();
            faculties.add(faculty);
            facultyRepository.save(faculties);

            User superAdministrator = UserFactory.make(UserFactory.SUPER_ADMINISTRATOR_TYPE);
            superAdministrator.setFaculty(faculty);
            userRepository.save(superAdministrator);

            List<User> technicalStaff = UserFactory.make(10, UserFactory.TECHNICAL_STAFF_TYPE);
            technicalStaff.forEach(technicalStaffPerson -> technicalStaffPerson.setFaculty(faculty));
            userRepository.save(technicalStaff);
        }

        // each application start should have its own study program where all relations will be added
        StudyProgram studyProgram = StudyprogramFactory.make();
        studyProgram.setFaculty(faculties.get(0));
        studyProgramRepository.save(studyProgram);

        createGradeCourseRelations(studyProgram, generatorIterations);

        createGradeCourseProfAndSubjectRelations(studyProgram, generatorIterations);
    }

    private void createGradeCourseRelations(StudyProgram studyProgram, int generatorIterations) {
        // generate professors
        User professor = UserFactory.make(UserFactory.PROFESSOR_TYPE);
        professor.setFaculty(studyProgram.getFaculty());
        userRepository.save(professor);

        // generate subjects
        Subject subject = SubjectFactory.make();
        subject.getStudyPrograms().add(studyProgram);
        subjectRepository.save(subject);

        logger.info("generating Grade-Course relation with {} iterations", generatorIterations);

        for (int i = 0; i < generatorIterations; i++) {
            double entities = Math.pow(10,i);
            logger.info("generating grade-course round {} [#courses={}]", i, entities);
            datasourceGenerator.generateGradeCourseRelation(professor, subject, entities);
        }

        logger.info("createGradeCourseRelations()::DONE");
    }

    /**
     * each iteration will generate
     * courses=10^floor(i/2)
     * grades=10^ceil(i/2).
     *
     * For 7 iterations, results are:
     * Grades       Courses
     * 1            1
     * 10           1
     * 10           10
     * 100          10
     * 100          100
     * 1000         100
     * 1000         1000
     */
    private void createGradeCourseProfAndSubjectRelations(StudyProgram studyProgram, int generatorIterations) {

        logger.info("generating Grade-Course-Professor/Subject relation with {} iterations", generatorIterations);

        for (int i = 0; i < generatorIterations; i++) {

            // generate professors
            User professor = UserFactory.make(UserFactory.PROFESSOR_TYPE);
            professor.setFaculty(studyProgram.getFaculty());
            userRepository.save(professor);

            // generate subjects
            Subject subject = SubjectFactory.make();
            subject.getStudyPrograms().add(studyProgram);
            subjectRepository.save(subject);

            double powerFloor = Math.floor((double)i/2); // courses
            double numberCourses = Math.pow(10, powerFloor);

            double powerCeil = Math.ceil((double)i/2); //grades
            double numberGrades = Math.pow(10,powerCeil);

            logger.info("generating Grade-Course-Professor/Subject round {} [#courses={}, #grades={}]."
                    , i, numberCourses, numberGrades);
            for (int j = 0; j < numberCourses; j++) {
                datasourceGenerator.generateGradeCourseRelation(professor, subject, numberGrades);
            }
        }

        logger.info("createGradeCourseProfAndSubjectRelations()::DONE");
    }
}
