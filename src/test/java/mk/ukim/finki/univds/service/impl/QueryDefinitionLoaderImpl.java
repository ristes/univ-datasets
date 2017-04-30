package mk.ukim.finki.univds.service.impl;

import mk.ukim.finki.univds.model.QueryDefinition;
import mk.ukim.finki.univds.service.TestSupportLoader;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Riste Stojanov
 */
@Service
public class QueryDefinitionLoaderImpl implements TestSupportLoader {


  private static String prefix = "PREFIX univ: <http://univ/> ";

  @Value("${app.paths.queries}")
  private String queriesDirectoryPath;

  @Override
  public List<QueryDefinition> loadDefinitions() throws IOException {
    List<QueryDefinition> allQueries = new ArrayList<>();

    File queriesDirectory = new File(queriesDirectoryPath);
    File[] queryCases = queriesDirectory.listFiles();

    for (File scenario : queryCases) {
      allQueries.addAll(loadScenario(scenario));
    }

    return allQueries;
  }


  private List<? extends QueryDefinition> loadScenario(File scenario) throws IOException {
    List<QueryDefinition> scenarioQueries = new ArrayList<>();

    File[] queryTemplates = scenario.listFiles();
    List<Map<String, String>> mapping = loadMapping(scenario);
    for (File template : queryTemplates) {
      if (!template.getName().endsWith(".rq")) {
        continue;
      }
      scenarioQueries.add(buildQueryDefinition(scenario.getName(), template, mapping));
    }
    return scenarioQueries;
  }

  private QueryDefinition buildQueryDefinition(String scenario, File template, List<Map<String, String>> mapping) throws IOException {
    List<String> lines = IOUtils.readLines(new FileInputStream(template), CharEncoding.UTF_8);
    String where = lines.stream().collect(Collectors.joining("\n"));
    QueryDefinition queryDefinition = new QueryDefinition();
    queryDefinition.setId(template.getName());
    queryDefinition.setParams(mapping);
    queryDefinition.setWhere(where);
    queryDefinition.setGraph("http://univ/" + scenario + "/" + template.getName());
    queryDefinition.setInsert(prefix + "WITH <" + queryDefinition.getGraph() + "> INSERT " + getGradeProjection());
    queryDefinition.setSelect(prefix + "CONSTRUCT " + getGradeProjection());
    return queryDefinition;
  }

  private List<Map<String, String>> loadMapping(File scenario) throws IOException {
    List<String> lines = IOUtils.readLines(new FileInputStream(new File(scenario, "_vars.tsv")), CharEncoding.UTF_8);
    String separatorRegex = "\t+";

    List<String> headers = Arrays.asList(lines.get(0).split(separatorRegex));

    return lines
      .stream()
      .skip(1)
      .map(
        l -> l.split(separatorRegex)
      )
      .filter(parts -> parts.length == headers.size())
      .map(parts -> {
        Map<String, String> mapping = new HashMap<String, String>();
        for (int i = 0; i < headers.size(); i++) {
          mapping.put(headers.get(i), parts[i]);
        }
        mapping.put("<gIRI>","<univ:StudyProgram/0>");
        return mapping;
      })
      .collect(Collectors.toList());
  }

  public String getGradeProjection() {
    return " { ?g ?p ?o } ";
  }
}