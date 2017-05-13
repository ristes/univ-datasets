package mk.ukim.finki.univds.testrealm;

import mk.ukim.finki.univds.testrealm.model.Measurement;
import mk.ukim.finki.univds.testrealm.model.service.impl.TdbQueryExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by KarateKid on 09.5.2017.
 */
@Service
public class CourseDrivenGeneratorEvaluation {

    private static final Logger logger = LoggerFactory.getLogger(CourseDrivenGeneratorEvaluation.class);

    @Value("${evaluation.runs:40}")
    private int RUNS;

    @Value("${evaluation.warmup:20}")
    private int WARM_UP;

    @Autowired
    private TdbQueryExecutor executor;

    // datasetCount == i
    // courseCount == j
    public void evaluate(int datasetCount, int courseCount, List<QueryHolder> queries) {
        for (int j = 0; j < datasetCount; j++) {
            for (int i = 1; i < courseCount; i++) {
                String dataSetSuffix = j + "" + i;
                executor.openDataset(dataSetSuffix);
                queries.forEach(query -> evaluateCourse(query.getQuery(), query.getName()));
                executor.closeDataset();
            }
        }
    }

    private void evaluateCourse(String query, String testCaseIri) {

        Measurement totalSelect = new Measurement(WARM_UP, null);
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