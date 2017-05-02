package mk.ukim.finki.univds.testrealm.model.service;


import mk.ukim.finki.univds.testrealm.model.QueryDefinition;

import java.io.IOException;
import java.util.List;

/**
 * @author Riste Stojanov
 */
public interface TestSupportLoader {

  List<QueryDefinition> loadDefinitions() throws IOException;

}
