package mk.ukim.finki.univds;

import mk.ukim.finki.univds.model.Measurement;
import mk.ukim.finki.univds.model.QueryDefinition;
import mk.ukim.finki.univds.service.QueryExecutor;
import mk.ukim.finki.univds.service.TestSupportLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Riste Stojanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = TestConfig.class)
public class TestCaseRunner {

  public static final String DATASET_PARAM = "dataset";
  public static final String RESULTS_PARAM = "results";

  @Autowired
  TestSupportLoader loader;

  @Autowired
  QueryExecutor executor;

  private String lastConstructQuery = "";


  @Test
  public void processScenario() {

    List<QueryDefinition> queries = loadQueries();

    for (QueryDefinition def : queries) {
      for (Map<String, String> params : def.getParams()) {
        for (int i = 0; i < 8; i++) {
          evaluateExecution(def, params, i);
        }
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

    long start = 0;
    if (constructQuery.equals(lastConstructQuery)) {
      return;
    }
    lastConstructQuery = constructQuery;

    executor.openDataset(dataset);

    executor.executeInsert(dataset, "DROP GRAPH <testIRI>");
    for (int i = 0; i < 12; i++) {
      executor.executeInsert(dataset, "DROP GRAPH " + testCaseIri);
      totalInsert.start(i);
      executor.executeInsert(dataset, insertQuery);
      totalInsert.pause(i);
    }
    totalInsert.print("INSERT", testCaseIri);
    executor.closeDataset();
    executor.openDataset(dataset);
    for (int i = 0; i < 12; i++) {
      totalSelect.start(i);
      executor.executeSelect(dataset, constructQuery);
      totalSelect.pause(i);
    }
    totalSelect.print("SELECT", testCaseIri);
    executor.closeDataset();
    System.out.println("===========================================================");
  }
}
