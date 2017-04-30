package mk.ukim.finki.univds.service;

import mk.ukim.finki.univds.model.QueryDefinition;
import org.apache.jena.query.Dataset;

import java.io.IOException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface TestSupportLoader {

  List<QueryDefinition> loadDefinitions() throws IOException;

}
