package mk.ukim.finki.univds.testrealm.model.service.impl;

import mk.ukim.finki.univds.testrealm.QueryResultsHolder;
import mk.ukim.finki.univds.testrealm.model.service.QueryExecutor;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Riste Stojanov
 */
@Service
public class TdbQueryExecutor implements QueryExecutor {

  private static final Logger logger = LoggerFactory.getLogger(TdbQueryExecutor.class);

  @Value("${app.paths.datasets.prefix}")
  private String datasetDirectoryPrefix;
  private static final int MB = 1024*1024;
  private Dataset dataset;

  @Override
  public long executeSelect(String dataSet, String queryString) {
    Query query = QueryFactory.create(queryString);

    // Execute the query and obtain results
    QueryExecution qe = QueryExecutionFactory.create(query, dataset);
    Model results = qe.execConstruct();
    long size = results.size();
    qe.close();
    return size;
  }

  @Override
  public QueryResultsHolder executeAndStoreSelect(String queryString) {
    Query query = QueryFactory.create(queryString);

    // Execute the query and obtain results
    QueryExecution qe = QueryExecutionFactory.create(query, dataset);
    Model results = qe.execConstruct();
    long size = results.size();
    QueryResultsHolder queryResultsHolder = new QueryResultsHolder();
    queryResultsHolder.setModel(results);
    queryResultsHolder.setResultSize(size);
    qe.close();
    return queryResultsHolder;
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

  @Override
  public Dataset getDataset() {
    return dataset;
  }

  @Override
  public void deleteNamedGraph(String iri) {
    dataset.removeNamedModel(iri);
  }

  private Dataset loadDataset(String name) {
    String directory = datasetDirectoryPrefix + name;
    logger.info("loading dataset: {}", directory);
    return TDBFactory.createDataset(directory);
  }

  private void printStatistics() {
    //Getting the runtime reference from system
    //Its singleton so its ok to get this every time
    Runtime runtime = Runtime.getRuntime();

    //Print free memory
    logger.info("Free Memory:"
            + runtime.freeMemory() / MB);

    //Print total available memory
    logger.info("Total Memory:" + runtime.totalMemory() / MB);

    //Print Maximum available memory
    logger.info("Max Memory:" + runtime.maxMemory() / MB);
  }
}
