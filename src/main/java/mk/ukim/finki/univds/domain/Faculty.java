package mk.ukim.finki.univds.domain;

import lombok.Getter;
import lombok.Setter;
import thewebsemantic.Namespace;
import thewebsemantic.RdfProperty;
import thewebsemantic.vocabulary.DublinCore;

import javax.persistence.Entity;

/**
 * Created by KarateKid on 04.2.2017.
 */
@Entity
@Getter
@Setter
@Namespace("http://univ#")
public class Faculty extends BaseEntity {

    @RdfProperty("http://www.w3.org/2000/01/rdf-schema#label")
    private String name;

    private String networkAddress;

}
