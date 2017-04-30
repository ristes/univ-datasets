package mk.ukim.finki.univds;

import mk.ukim.finki.univds.model.Measurement;
import mk.ukim.finki.univds.model.QueryDefinition;
import mk.ukim.finki.univds.service.QueryExecutor;
import mk.ukim.finki.univds.service.TestSupportLoader;
import mk.ukim.finki.univds.service.impl.QueryDefinitionLoaderImpl;
import mk.ukim.finki.univds.service.impl.TdbQueryExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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
  @Autowired
  TestSupportLoader loader;

  @Autowired
  QueryExecutor executor;


  @Test
  public void processScenario() {

    List<QueryDefinition> queries = loadQueries();

    for (QueryDefinition def : queries) {
      for (Map<String, String> params : def.getParams()) {
        evaluateExecution(def, params);
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

  private void evaluateExecution(QueryDefinition def, Map<String, String> params) {
    String dataset = params.get(DATASET_PARAM);
    String where = def.getWhere();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      where = where.replaceAll(entry.getKey(), entry.getValue());
    }

    Measurement totalSelect = new Measurement(2, def);
    Measurement totalInsert = new Measurement(2, def);

    long start = 0;
    for (int i = 0; i < 12; i++) {
      totalInsert.start(i);
      executor.executeInsert(dataset, def.getInsert() + where);
      totalInsert.pause(i);
      totalSelect.start(i);
      executor.executeSelect(dataset, def.getSelect() + where);
      totalSelect.pause(i);
    }

    totalInsert.print("INSERT", dataset);
    totalSelect.print("SELECT", dataset);
  }
}
