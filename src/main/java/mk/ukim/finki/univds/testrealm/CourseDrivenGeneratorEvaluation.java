package mk.ukim.finki.univds.testrealm;

import mk.ukim.finki.univds.testrealm.model.Measurement;
import mk.ukim.finki.univds.testrealm.model.service.impl.TdbQueryExecutor;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    @Value("${save.generated.constructs:false}")
    private Boolean serializeQuery;

    @Value("${save.generated.constructs.directory:}")
    private String serializeQueryDirectory;

    @Value("${course.count.start:1}")
    private int courseCountStart;

    @Value("${dataset.count.start:0}")
    private int datasetCountStart;

    @Autowired
    private TdbQueryExecutor executor;

    // datasetCount == i
    // courseCount == j
    public void evaluate(int datasetCount, int courseCount, List<QueryHolder> queries) {
        for (int j = datasetCountStart; j < datasetCount; j++) {
            for (int i = courseCountStart; i < courseCount; i++) {
                String dataSetSuffix = j + "" + i;
                executor.openDataset(dataSetSuffix);
                queries.forEach(query -> evaluateCourse(query.getQuery(), query.getName()));
                executor.closeDataset();
            }
        }
    }

    private void evaluateCourse(String query, String testCaseIri) {

        Measurement totalSelect = new Measurement(WARM_UP, null);
        QueryResultsHolder queryResultsHolder = new QueryResultsHolder();
        for (int run = 0; run < RUNS; run++) {
            totalSelect.start(run);
            queryResultsHolder = executor.executeAndStoreSelect(query);
            totalSelect.pause(run);

            if (serializeQuery) {
                String filename = sanitizeName(testCaseIri);
                serializeModel(queryResultsHolder.getModel(), filename);
            }
        }
        totalSelect.print("SELECT\t" + queryResultsHolder.getResultSize(), testCaseIri);
        logger.info("===========================================================");
    }

    private void serializeModel(Model model, String filename) {
        try(OutputStream outputStream = Files.newOutputStream(Paths.get(serializeQueryDirectory,filename))) {
            RDFDataMgr.write(outputStream, model, Lang.NTRIPLES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String sanitizeName(String name) {
        return name.split("\\.")[0];
    }

}