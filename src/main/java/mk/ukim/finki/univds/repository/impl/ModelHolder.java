package mk.ukim.finki.univds.repository.impl;


import org.apache.commons.io.FileUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Riste Stojanov
 */
public class ModelHolder {


  public static String TDB_ROOT_DIR = "ds/tdb/";
  private static final String TDB_DATASET_DIR = TDB_ROOT_DIR + "ds";

  private static Logger logger = LoggerFactory.getLogger(ModelHolder.class);

  private static Dataset dataSet;

  public static Dataset getDataSource() {
    if (dataSet == null) {
      throw new IllegalStateException("Create or reset dataset first!");
    }
    return dataSet;
  }

  public static Dataset resetDataset(int newIndex) throws IOException {
    int currentIndex = newIndex - 1;
    String currentDatasetDirectoryPath = TDB_DATASET_DIR + currentIndex;
    String nextDatasetDirectoryPath = TDB_DATASET_DIR + newIndex;

    logger.info("Resetting dataset started. Flushing {}", currentDatasetDirectoryPath);
    dataSet.close();
    logger.info("Successfully flushed {}", currentDatasetDirectoryPath);

    File currentDatasetDirectory = new File(currentDatasetDirectoryPath);
    File nextDatasetDirectory = new File(nextDatasetDirectoryPath);

    logger.info("Resetting dataset. Copying current dataset contents into {}.", nextDatasetDirectoryPath);
    FileUtils.copyDirectory(currentDatasetDirectory, nextDatasetDirectory);

    logger.info("copying done. Now connecting to the new dataset directory.");

    dataSet = openDataset("ds" + newIndex);
    logger.info("Resetting done. Now dataset points in {}.", nextDatasetDirectoryPath);
    return dataSet;
  }

  public static Dataset createInitialDataset(String name) {
    dataSet = openDataset(name);
    Univ.init(dataSet.getDefaultModel());
    return dataSet;
  }

  public static Dataset openDataset(String name) {
    // Make a TDB-backed dataset
    String directory = TDB_ROOT_DIR + name;
    logger.info("creating dataset under: {}", directory);
    File f = new File(directory);
    if (!f.exists()) {
      f.mkdirs();
    }
    dataSet = TDBFactory.createDataset(directory);
    return dataSet;
  }

  public static void closeDataset() {
    if (dataSet == null) {
      throw new IllegalStateException("Cannot close null dataSet!");
    }
    dataSet.close();
    dataSet = null;
  }

  public static Model getDefaultModel() {
    return getDataSource().getDefaultModel();
  }

  public static Model getNamedGraph(String iri) {
    return getDataSource().getNamedModel(iri);
  }

  public static Model createNamedModel(String modelUri, Model namedModel) {
    getDataSource().addNamedModel(modelUri, namedModel);
    return namedModel;
  }

  public static Model createModel() {
    return ModelFactory.createDefaultModel();
  }

  public static boolean containsResource(Model m, String iri) {
    return m.containsResource(Univ.getInstance(m, iri));
  }

  public static boolean containsResourceInDefaultModel(String iri) {
    Model defaultModel = getDataSource().getDefaultModel();
    return defaultModel.containsResource(Univ.getInstance(defaultModel, iri));
  }

  public static void print(OutputStream outputStream, Dataset dataSource) {
//        RDFDataMgr.write(System.out, dataSource, Lang.NQUADS);

    Model defaultModel = dataSource.getDefaultModel();
    logger.info("====== DEFAULT GRAPH CONTENT ======");
    RDFDataMgr.write(outputStream, defaultModel, Lang.NTRIPLES);

    dataSource.listNames().forEachRemaining(namedGraph -> {
      logger.info("");
      logger.info("");
      logger.info("====== NAMED GRAPH " + namedGraph + " CONTENT ======");
      RDFDataMgr.write(outputStream, dataSource.getNamedModel(namedGraph), Lang.NTRIPLES);
    });
  }
}
