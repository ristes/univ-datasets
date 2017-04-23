package mk.ukim.finki.univds.repository.impl;


import mk.ukim.finki.univds.domain.StudyProgram;
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
      initializeResources();
    }
    return dataSet;
  }

  private static void initializeResources() {
    Model m = dataSet.getDefaultModel();
    m.createResource(StudyProgram.CLASS);

    m.createProperty(StudyProgram._NAME);
    m.createResource(StudyProgram._FACULTY);

  }

  private static Dataset createDatasource() {
    return DatasetFactory.create();

  }


  public static void loadModel(String file) {

  }

  public static Model getDefaultModel() {
    return dataSet.getDefaultModel();
  }

  public static Model createNamedModel(String modelUri) {
    Model namedModel = createModel();
    getDataSource().addNamedModel(modelUri, namedModel);
    return namedModel;
  }

  private static Model createModel() {
    return ModelFactory.createDefaultModel();
  }




}
