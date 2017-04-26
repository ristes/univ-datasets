package mk.ukim.finki.univds;

import mk.ukim.finki.univds.domain.Faculty;
import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.generator.DatasourceGenerator;
import mk.ukim.finki.univds.generator.GeneratorService;
import mk.ukim.finki.univds.generator.factories.StudyprogramFactory;
import mk.ukim.finki.univds.repository.StudyprogramRepository;
import mk.ukim.finki.univds.repository.impl.ModelHolder;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.OutputStream;

/**
 * Runs spring console application.
 */
@Component
public class ConsoleApplicationRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ConsoleApplicationRunner.class);

    @Autowired
    private GeneratorService generatorService;

    @Autowired
    private StudyprogramRepository studyProgramRepository;

    @Autowired
    private DatasourceGenerator noiceDatasourceGenerator;

    @Override
    public void run(String... args) throws Exception {
        logger.info("application started with {} iteration cycles" + args[0]);

        ModelHolder.createInitialDataset("ds0");
        int generatorCycles = Integer.parseInt(args[0]);
        Faculty faculty = generatorService.generate(generatorCycles);
        System.out.println("DS0 coming up!");
        print(System.out);

        ModelHolder.resetDataset("ds1");

        StudyProgram studyProgram = StudyprogramFactory.make();
        studyProgram.setFaculty(faculty);
        studyProgramRepository.save(studyProgram);

        noiceDatasourceGenerator.generateData(1,1,1, faculty, studyProgram);

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("DS1 coming up!");
        print(System.out);

        logger.info("I am done!");
    }

    private void print(OutputStream outputStream) {
        Dataset dataSource = ModelHolder.getDataSource();
//        RDFDataMgr.write(System.out, dataSource, Lang.NQUADS);

        Model defaultModel = dataSource.getDefaultModel();
        System.out.println("====== DEFAULT GRAPH CONTENT ======");
        RDFDataMgr.write(outputStream, defaultModel, Lang.NTRIPLES);

        dataSource.listNames().forEachRemaining(namedGraph -> {
            System.out.println();
            System.out.println();
            System.out.println("====== NAMED GRAPH " + namedGraph + " CONTENT ======");
            RDFDataMgr.write(outputStream, dataSource.getNamedModel(namedGraph), Lang.NTRIPLES);
        });
    }
}
