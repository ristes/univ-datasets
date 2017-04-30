package mk.ukim.finki.univds.service.impl;

import mk.ukim.finki.univds.service.QueryExecutor;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Riste Stojanov
 */
@Service
public class TdbQueryExecutor implements QueryExecutor {


  @Value("${app.paths.datasets.prefix}")
  private String datasetDirectoryPrefix;

  private Dataset dataset;

  @Override
  public long executeSelect(String dataSet, String queryString) {
    Query query = QueryFactory.create(queryString);

    // Execute the query and obtain results
    QueryExecution qe = QueryExecutionFactory.create(query, dataset);
    Model results = qe.execConstruct();
    long size = results.size();
    // Important - free up resources used running the query
    qe.close();
    return size;
  }

  @Override
  public void executeInsert(String dataSet, String queryString) {
    UpdateAction.parseExecute(queryString, dataset);
  }

  public void openDataset(String dataSet) {
    if (dataset != null) {
      dataset.close();
    }
    dataset = loadDataset(dataSet);
  }

  public void closeDataset() {
    dataset.close();
    dataset = null;
  }

  private Dataset loadDataset(String name) {
    String directory = datasetDirectoryPrefix + name;
    return TDBFactory.createDataset(directory);
  }
}
