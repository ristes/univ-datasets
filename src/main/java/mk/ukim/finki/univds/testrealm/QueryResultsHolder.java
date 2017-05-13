package mk.ukim.finki.univds.testrealm;

import lombok.Getter;
import lombok.Setter;
import org.apache.jena.rdf.model.Model;

/**
 * Created by KarateKid on 13.5.2017.
 */
@Getter
@Setter
public class QueryResultsHolder {

    private Model model;
    private long resultSize;

}
