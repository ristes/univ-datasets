package mk.ukim.finki.univds;

import mk.ukim.finki.univds.model.Measurement;
import mk.ukim.finki.univds.model.QueryDefinition;
import mk.ukim.finki.univds.service.QueryExecutor;
import mk.ukim.finki.univds.service.TestSupportLoader;
import org.apache.jena.ext.com.google.common.collect.Lists;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Riste Stojanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ContextConfiguration(classes = TestConfig.class)
public class GraphCleaner {

  private static final Logger logger = LoggerFactory.getLogger(GraphCleaner.class);
  public static final String DATASET_PARAM = "dataset";
  public static final String RESULTS_PARAM = "results";

  @Autowired
  TestSupportLoader loader;

  @Autowired
  QueryExecutor executor;

  private String lastConstructQuery = "";


  @Test
  public void clearDatasets() {
    for (int i = 0; i < 6; i++) {
      executor.openDataset(""+i);
      Dataset dataset = executor.getDataset();
      logger.info("========== LISTING DS" + i + " NAMED GRAPHS ========");
//      dataset.listNames().forEachRemaining(System.out::println);

      List<String> graphsToRemove = Lists.newArrayList();
      dataset.listNames().forEachRemaining(graphName -> {
        if(graphName.contains("test")) {
          graphsToRemove.add(graphName);
        }
      });

      logger.info("will remove " + graphsToRemove.size() );
      graphsToRemove.forEach(graphName -> {
        dataset.removeNamedModel(graphName);
      });

      if(graphsToRemove.size() > 0) {
        dataset.close();
      }
    }
  }

  protected List<QueryDefinition> loadQueries() {
    try {
      return loader.loadDefinitions();
    } catch (IOException e) {
      return Collections.emptyList();
    }
  }

}
