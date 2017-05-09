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
import mk.ukim.finki.univds.repository.impl.ModelHolder;
import mk.ukim.finki.univds.repository.impl.StaffRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Generates dataset where course is the central entity in the generating process.
 */
@Service
public class CourseDrivenGeneratorService {

  private static final Logger logger = LoggerFactory.getLogger(CourseDrivenGeneratorService.class);

  @Autowired
  private FacultyRepository facultyRepository;

  @Autowired
  private StaffRepositoryImpl staffRepository;

  @Autowired
  private ControlledDatasourceGenerator datasourceGenerator;

  @Autowired
  private StudyprogramRepository studyProgramRepository;

  @Autowired
  private SubjectRepository subjectRepository;

  @Value("${app.paths.datasets.prefix}")
  private String datasetDirectoryPrefix;

  // datasetCount == i
  // courseCount == j
  public void generate(int datasetCount, int courseCount) {

    ModelHolder.TDB_ROOT_DIR = datasetDirectoryPrefix;
    logger.info("generating started. datasets will be stored in: {}", ModelHolder.TDB_ROOT_DIR);
    // ============== generate base
    // =========================================
    // =========================================
    // =========================================
    Faculty faculty = FacultyFactory.make();

    User superAdministrator = UserFactory.make(UserFactory.SUPER_ADMINISTRATOR_TYPE);
    superAdministrator.setFaculty(faculty);

    List<User> technicalStaff = UserFactory.make(3, UserFactory.TECHNICAL_STAFF_TYPE);
    technicalStaff.forEach(technicalStaffPerson -> technicalStaffPerson.setFaculty(faculty));

    // each application start should have its own study program where all relations will be added
    StudyProgram studyProgram = StudyprogramFactory.make();
    studyProgram.setFaculty(faculty);
    // =========================================
    // =========================================
    // =========================================
    for (int j = 0; j < datasetCount; j++) {
      for (int i = 1; i < courseCount; i++) {
        String datasetName = j + "" + i;
        logger.info("now generating {}", datasetName);
        ModelHolder.createInitialDataset(datasetName);

        // save the base so its the same for all ds. This can be done better...
        facultyRepository.save(faculty);
        staffRepository.save(superAdministrator);
        staffRepository.save(technicalStaff);
        studyProgramRepository.save(studyProgram);
        // base saved.

        // generate professors
        User professor = UserFactory.make(UserFactory.PROFESSOR_TYPE);
        professor.setFaculty(studyProgram.getFaculty());
        staffRepository.save(professor);

        // generate subjects
        Subject subject = SubjectFactory.make();
        subject.getStudyPrograms().add(studyProgram);
        subjectRepository.save(subject);

        for (int k = 0; k <= j; k++) {
          for (int t = 1; t < courseCount; t++) {
            double numberOfGrades = t * 100 * Math.pow(10, k);
            Long courseId = (long) 10 * k + t;
            datasourceGenerator.generateGradeCourseRelation(professor, subject, numberOfGrades, courseId);
          }
        }
        ModelHolder.closeDataset();
      }
    }
  }
}
