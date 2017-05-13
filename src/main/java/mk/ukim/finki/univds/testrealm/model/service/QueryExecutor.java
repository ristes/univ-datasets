package mk.ukim.finki.univds.testrealm.model.service;

import mk.ukim.finki.univds.testrealm.QueryResultsHolder;
import org.apache.jena.query.Dataset;

/**
 * @author Riste Stojanov
 */
public interface QueryExecutor {

  long executeSelect(String dataSet, String query);

  QueryResultsHolder executeAndStoreSelect(String queryString);

  void executeInsert(String dataSet, String query);

  void openDataset(String dataSet);

  void closeDataset();

  Dataset getDataset();

  void deleteNamedGraph(String iri);

}
