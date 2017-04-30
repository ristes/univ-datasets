package mk.ukim.finki.univds.service;

/**
 * @author Riste Stojanov
 */
public interface QueryExecutor {

  long executeSelect(String dataSet, String query);

  void executeInsert(String dataSet, String query);

  void openDataset(String dataSet);

  void closeDataset();


}
