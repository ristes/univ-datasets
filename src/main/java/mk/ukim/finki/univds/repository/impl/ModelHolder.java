package mk.ukim.finki.univds.repository.impl;


import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

/**
 * @author Riste Stojanov
 */
public class ModelHolder {

  private static Dataset dataSet;

  public static Dataset getDataSource() {
    if (dataSet == null) {
      dataSet = createDatasource();
      Univ.init(dataSet.getDefaultModel());
    }
    return dataSet;
  }

  public static Dataset resetDataSource() {
    Model defaultModel = getDataSource().getDefaultModel();
    dataSet = null;
    dataSet = getDataSource();
    dataSet.setDefaultModel(defaultModel);
    return dataSet;
  }

  private static Dataset createDatasource() {
    return DatasetFactory.create();
  }


  public static void loadModel(String file) {
    throw new NotImplementedException("@TODO: method not implemented");
  }

  public static Model getDefaultModel() {
    return getDataSource().getDefaultModel();
  }

  public static Model getNamedGraph(String iri) { return getDataSource().getNamedModel(iri); }

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
