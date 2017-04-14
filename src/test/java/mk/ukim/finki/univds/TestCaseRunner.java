package mk.ukim.finki.univds;

import mk.ukim.finki.univds.model.Measurement;
import mk.ukim.finki.univds.model.QueryDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author Riste Stojanov
 */
public abstract class TestCaseRunner {

  abstract List<QueryDefinition> loadQueries();

  abstract List<String> loadDataSets();

  abstract void executeSelect(String dataSet, String query);

  abstract void executeInsert(String dataSet, String query);


  public void processScenario() {
    List<String> datasets = loadDataSets();
    List<QueryDefinition> queries = loadQueries();

    for (String dataset : datasets) {
      for (QueryDefinition def : queries) {
        for (Map<String, String> params : def.getParams()) {
          evaluateExecution(dataset, def, params);
        }
      }
    }
  }

  private void evaluateExecution(String dataset, QueryDefinition def, Map<String, String> params) {
    String where = def.getWhere();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      where = where.replaceAll(entry.getKey(), entry.getValue());
    }

    Measurement totalSelect = new Measurement(10, def);
    Measurement totalInsert = new Measurement(10, def);

    long start = 0;
    for (int i = 0; i < 110; i++) {
      totalInsert.start(i);
      executeInsert(dataset, def.getInsert() + where);
      totalInsert.pause(i);
      totalSelect.start(i);
      executeSelect(dataset, def.getSelect() + where);
      totalSelect.pause(i);
    }

    totalInsert.print("INSERT", dataset);
    totalSelect.print("SELECT", dataset);
  }
}
