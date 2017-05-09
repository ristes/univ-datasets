package mk.ukim.finki.univds;

import mk.ukim.finki.univds.generator.CourseDrivenGeneratorService;
import mk.ukim.finki.univds.repository.impl.ModelHolder;
import mk.ukim.finki.univds.testrealm.CourseDrivenGeneratorEvaluation;
import org.apache.jena.rdf.model.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Run the course driven generator service.
 */
@Component
public class CourseDrivenGeneratorApplicationRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(CourseDrivenGeneratorApplicationRunner.class);

    @Autowired
    private CourseDrivenGeneratorService generatorService;

    @Autowired
    private CourseDrivenGeneratorEvaluation evaluator;

    @Override
    public void run(String... args) throws Exception {

        int datasetCount = Integer.parseInt(args[0]);
        int courseCount = Integer.parseInt(args[1]);
        String pathToQueryFile = args[2];
        boolean generateDatasources = Boolean.parseBoolean(args[3]);
        if (generateDatasources) {
            generatorService.generate(datasetCount, courseCount);
            logger.info("data generation done.");
        }
        logger.info("evaluating data with query located in {}", pathToQueryFile);
        evaluator.evaluate(datasetCount, courseCount, pathToQueryFile);
    }
}
