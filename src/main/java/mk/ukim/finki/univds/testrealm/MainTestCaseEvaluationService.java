package mk.ukim.finki.univds.testrealm;

import mk.ukim.finki.univds.testrealm.model.Measurement;
import mk.ukim.finki.univds.testrealm.model.QueryDefinition;
import mk.ukim.finki.univds.testrealm.model.service.QueryExecutor;
import mk.ukim.finki.univds.testrealm.model.service.TestSupportLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Runs the stuff needed for the evaluation.
 */
@Service
public class MainTestCaseEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(MainTestCaseEvaluationService.class);

    public static final String DATASET_PARAM = "dataset";
    public static final String RESULTS_PARAM = "results";
    public static final int RUNS = 7; // 2 ignorira 5 broi

    @Autowired
    private TestSupportLoader loader;

    @Autowired
    private QueryExecutor executor;

    private String lastConstructQuery = "";


    public void processScenario() {
        // sorted in _vars.tsv (all _vars.tsv)
        List<QueryDefinition> queries = loadQueries();

        queries.forEach(queryDefinition -> {
            logger.info(queryDefinition.getId());
        });

        // this does all the job. uncomment it when done analyzing.
        for (QueryDefinition def : queries) {
            for (Map<String, String> params : def.getParams()) {
                String dataset = params.get(DATASET_PARAM);
                Integer c = Integer.parseInt(dataset);
                evaluateExecution(def, params, 1);
            }
        }

    }

    protected List<QueryDefinition> loadQueries() {
        try {
            return loader.loadDefinitions();
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private void evaluateExecution(QueryDefinition def, Map<String, String> params, int grahId) {
        String dataset = params.get(DATASET_PARAM);
        String results = params.get(RESULTS_PARAM);

        String testCaseIri = String.format("<http://univ/test/%s/ds-%s-res-%s-gr-%d>", def.getId(), dataset, results, grahId);
        params.put("<gIRI>", "<http://univ/StudyProgram/" + grahId + ">");
        params.put(
                "<testIRI>",
                testCaseIri
        );

        String where = def.getWhere();
        String constructQuery = def.getSelect() + "\n" + where;
        String insertQuery = def.getInsert() + "\n" + where;


        for (Map.Entry<String, String> entry : params.entrySet()) {
            insertQuery = insertQuery.replaceAll(entry.getKey(), entry.getValue());
            constructQuery = constructQuery.replaceAll(entry.getKey(), entry.getValue());
        }

        Measurement totalSelect = new Measurement(2, def);
        Measurement totalInsert = new Measurement(2, def);
        Measurement open = new Measurement(2, def);
        Measurement close = new Measurement(2, def);
        Measurement delete = new Measurement(2, def);


//        open.start(2);
//        executor.openDataset(dataset);
//        open.pause(2);

        delete.start(2);
        // executor.deleteNamedGraph(testCaseIri);
        delete.pause(2);
        for (int i = 0; i < RUNS; i++) {
            delete.start(i + 3);
            // executor.deleteNamedGraph(testCaseIri);
            delete.pause(i + 3);
            totalInsert.start(i);
            //executor.executeInsert(dataset, insertQuery);
            totalInsert.pause(i);
        }
        totalInsert.print("INSERT", testCaseIri);
//        close.start(2);
//        executor.closeDataset();
//        close.pause(2);
        open.start(3);
        executor.openDataset(dataset);
        open.pause(3);
        long resultsSize = 0;
        for (int i = 0; i < RUNS; i++) {
            totalSelect.start(i);
            resultsSize = executor.executeSelect(dataset, constructQuery);
            totalSelect.pause(i);
        }
        totalSelect.print("SELECT\t" + resultsSize, testCaseIri);

        delete.start(15);
//    executor.deleteNamedGraph(testCaseIri);
        delete.pause(15);
        close.start(3);
        executor.closeDataset();
        close.pause(3);
        open.print("OPEN", testCaseIri);
        close.print("CLOSE", testCaseIri);
        delete.print("DELETE", testCaseIri);
        logger.info("===========================================================");
    }
}
