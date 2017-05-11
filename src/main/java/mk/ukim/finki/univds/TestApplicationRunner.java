package mk.ukim.finki.univds;

import mk.ukim.finki.univds.unrelated.StaticQueryBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Ignore this. Used only for tryouts during development.
 */
public class TestApplicationRunner {

    private static String querySceleton;
    private static String bodyPart;

    public static void main(String[] args) throws IOException {

        querySceleton = "PREFIX univ: <http://univ/>\n" +
                "PREFIX usr: <http://univ/User/>\n" +
                "PREFIX sp: <http://univ/StydyProgram/>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "\n" +
                "CONSTRUCT '{'\n" +
                "    ?g ?p ?o\n" +
                "'}' WHERE '{'\n" +
                "{0}" +
                "'}'\n";
        bodyPart =
                "\t?g univ:for_course <http://univ/Course/{0}>.\n" +
                "\t?g ?p ?o";


//        generateQuery(1,3);
//        generateQuery(4,6);
        generateQuery(1,6);

//        StaticQueryBuilder builder = new StaticQueryBuilder(querySceleton, bodyPart);
//        for (int i = 0; i < 4; i++) {
//            if (i==0) {
//                 builder.appendBodyPart("1").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart("2").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart("3").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart("1").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart("4").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart("2").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart("5").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart("3").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart("6");
//            } else {
//                builder.appendBodyPart(i+"1").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart(i+"2").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart(i+"3").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart(i+"1").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart(i+"4").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart(i+"2").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart(i+"5").appendOperation(StaticQueryBuilder.Operations.MINUS)
//                        .appendBodyPart(i+"3").appendOperation(StaticQueryBuilder.Operations.UNION)
//                        .appendBodyPart(i+"6");
//            }
//            String fileName = "longQuery-C" + i + ".rq";
//            String query = builder.build();
//            System.out.println(query);
//            Files.write(Paths.get(fileName), query.getBytes());
//        }

    }

    private static void generateQuery(int subsetFrom, int subsetTo) throws IOException {
        StaticQueryBuilder builder = new StaticQueryBuilder(querySceleton, bodyPart);
        for (int i = 0; i < 4; i++) {
            for (int j = subsetFrom; j < subsetTo + 1; j++) {
                int courseId = i*10 + j;
                builder = builder.appendBodyPart("" + courseId);
                save(builder, i, j);
//                System.out.println(i*10 + j);
            }
        }
    }

    private static void save(StaticQueryBuilder builder, int i, int j) throws IOException {
        String fileName = "f" + i + "" + j + ".rq";
        System.out.println("Just created: " + fileName);
        String query = builder.build();
        System.out.println(query);
        Files.write(Paths.get(fileName), query.getBytes());
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
