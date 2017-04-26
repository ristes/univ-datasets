package mk.ukim.finki.univds.repository.impl;


import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.tdb.TDBFactory;

import java.io.File;

/**
 * @author Riste Stojanov
 */
public class ModelHolder {

  private static Dataset dataSet;

  public static Dataset getDataSource() {
    if (dataSet == null) {
      throw new IllegalStateException("Create or reset dataset first!");
    }
    return dataSet;
  }


  public static Dataset resetDataset(String name) {
    Model defaultModel = getDataSource().getDefaultModel();
    Dataset old = dataSet;
    dataSet = createDataset(name);
    dataSet.getDefaultModel().add(defaultModel);
    old.close();
    return dataSet;
  }

  public static Dataset createInitialDataset(String name) {
    dataSet = createDataset(name);
    Univ.init(dataSet.getDefaultModel());
    return dataSet;
  }

  private static Dataset createDataset(String name) {
    // Make a TDB-backed dataset
    String directory = "ds/tdb/" + name;
    File f=new File(directory);
    if(!f.exists()) {
      f.mkdirs();
    }
    dataSet = TDBFactory.createDataset(directory);
    return dataSet;
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
}
