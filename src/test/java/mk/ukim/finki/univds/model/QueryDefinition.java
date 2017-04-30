package mk.ukim.finki.univds.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * @author Riste Stojanov
 */
@Getter
@Setter
public class QueryDefinition {

  private String id;

  private List<Map<String, String>> params;

  private String where;

  private String insert;

  private String select;

  private String graph;

}
