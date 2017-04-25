package mk.ukim.finki.univds;

/**
 * Ignore this. Used only for tryouts during development.
 */
public class TestApplicationRunner {

    public static void main(String[] args) {

    }


//        StudyProgramRepositoryImpl studyProgramRepository = new StudyProgramRepositoryImpl();
//        FacultyRepositoryImpl facultyRepository = new FacultyRepositoryImpl();
//        SubjectRepositoryImpl subjectRepository = new SubjectRepositoryImpl();
//        StaffRepositoryImpl staffRepository = new StaffRepositoryImpl();
//        StudentRepositoryImpl studentRepository = new StudentRepositoryImpl();
//
//        Faculty faculty = FacultyFactory.make();
//        facultyRepository.save(faculty);
//
//        StudyProgram studyProgram = StudyprogramFactory.make();
//        studyProgram.setFaculty(faculty);
//        studyProgramRepository.save(studyProgram);
//
//        Subject subject = SubjectFactory.make();
//        subject.getStudyPrograms().add(studyProgram);
//        subjectRepository.save(subject);
//
//        User professor = UserFactory.make(UserFactory.PROFESSOR_TYPE);
//        professor.setFaculty(faculty);
//        staffRepository.save(professor);
//
//        User student = UserFactory.make(UserFactory.STUDENT_TYPE);
//        student.setStudyProgram(studyProgram);
//        studentRepository.save(student);
//
//
//
//        System.out.println("====== DEFAULT GRAPH CONTENT ======");
//        RDFDataMgr.write(System.out, ModelHolder.getDefaultModel(), Lang.NTRIPLES);
//
//        System.out.println(); // just an empty line
//        System.out.println("====== NAMED STUDY PROGRAM GRAPH CONTENT ======");
//        Model studyProgramNamedGraph = ModelHolder.getDataSource().getNamedModel("http://univ/StudyProgram/1");
//        RDFDataMgr.write(System.out, studyProgramNamedGraph, Lang.NTRIPLES) ;

//        String filename = "C:\\Users\\KarateKid\\Desktop\\ds1.nt\\current.nt";
//        Dataset dataSource = ModelHolder.getDataSource();
//        OutputStream outputStream = Files.newOutputStream(Paths.get(filename));
//        RDFDataMgr.write(outputStream, dataSource, Lang.NQUADS);

//        String filename = "C:\\Users\\KarateKid\\Desktop\\ds1.nt\\ds3.nt";
//        InputStream inputStream = Files.newInputStream(Paths.get(filename));
//        Dataset dataSource = DatasetFactory.create();
//        RDFDataMgr.read(dataSource, inputStream, Lang.NQUADS);
//
//        RDFDataMgr.write(System.out, dataSource, Lang.NQUADS);

}
