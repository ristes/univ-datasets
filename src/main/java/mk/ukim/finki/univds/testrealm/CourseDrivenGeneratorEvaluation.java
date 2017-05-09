package mk.ukim.finki.univds.testrealm;

import com.sun.org.apache.xpath.internal.operations.Mod;
import mk.ukim.finki.univds.repository.impl.ModelHolder;
import mk.ukim.finki.univds.testrealm.model.Measurement;
import mk.ukim.finki.univds.testrealm.model.service.impl.TdbQueryExecutor;
import org.apache.jena.base.Sys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

/**
 * Created by KarateKid on 09.5.2017.
 */
@Service
public class CourseDrivenGeneratorEvaluation {

    private static final Logger logger = LoggerFactory.getLogger(CourseDrivenGeneratorEvaluation.class);

    private static final int RUNS = 7;
    @Autowired
    private TdbQueryExecutor executor;

    // datasetCount == i
    // courseCount == j
    public void evaluate(int datasetCount, int courseCount, String pathToQuery) throws IOException {
        String query = Files.readAllLines(Paths.get(pathToQuery))
                .stream()
                .collect(Collectors.joining("\n"));
        query = query.replace("gIRI", "http://univ/StudyProgram/1");

        for (int j = 1; j < datasetCount; j++) {
            for (int i = 1; i < courseCount; i++) {
                executor.openDataset(j + "" + i);
//                ModelHolder.print(System.out, executor.getDataset());
                for (int k = 0; k < j; k++) {
                    for (int t = 1; t < courseCount; t++) {
                        int courseId = 10 * k + t;
                        String testCaseIri = "http://univ/Course/" + courseId;
                        query = query.replace("cIRI", testCaseIri);
                        evaluateCourse(query, testCaseIri);
//        evaluateCourse(10*k+t, dataset,queryString);// pravi replace na queryString so courseIRI pred da go izvrsi
                    }
                }
                executor.closeDataset();
            }
        }
    }

    private void evaluateCourse(String query, String testCaseIri) {

        Measurement totalSelect = new Measurement(2, null);
        long resultsSize = 0;
        for (int run = 0; run < RUNS; run++) {
            totalSelect.start(run);
            resultsSize = executor.executeSelect(null, query);
            totalSelect.pause(run);
        }
        totalSelect.print("SELECT\t" + resultsSize, testCaseIri);
        logger.info("===========================================================");
    }

}