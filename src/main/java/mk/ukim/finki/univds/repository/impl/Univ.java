package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * @author Riste Stojanov
 */
public class Univ {

  public static Resource FacultyResource;
  public static Resource StudyProgramResource;
  public static Resource SubjectResource;
  public static Resource CourseResource;
  public static Resource UserResource;
  public static Resource GradeResource;

  // common properties
  public static Property rdfLabel;
  public static Property rdfType;
  public static Property name;

  // faculty properties
  public static Property faculty;
  public static Property networkAddress;

  // subject properties
  public static Property description;
  public static Property study_program;

  // user properties
  public static Property email;
  public static Property phone;
  public static Property works_at;
  public static Property enrolled_at;
  public static Property DTYPE;

  // course properties
  public static Property year;
  public static Property has_professor;
  public static Property subject;
  public static Property for_student; // - common with course

  // grade properties
  public static Property for_course;
  public static Property grade_value;


  public static void init(Model m) {
    FacultyResource = m.createResource(Faculty.RDF_INSTANCE);
    StudyProgramResource = m.createResource(StudyProgram.RDF_INSTANCE);
    SubjectResource = m.createResource(Subject.RDF_INSTANCE);
    // professor + student
    UserResource = m.createResource(User.RDF_INSTANCE);
    CourseResource = m.createResource(Course.RDF_INSTANCE);
    GradeResource = m.createResource(Grade.RDF_INSTANCE);

    rdfLabel = RDFS.label;

    rdfType = RDF.type;
    faculty = m.createProperty("http://univ/faculty");
    name = m.createProperty("http://univ/name");
    networkAddress = m.createProperty("http://univ/networkAddress");
    description = m.createProperty("http://univ/description");
    study_program = m.createProperty("http://univ/study_program");
    email = m.createProperty("http://univ/email");
    phone = m.createProperty("http://univ/phone");
    works_at = m.createProperty("http://univ/works_at");
    enrolled_at = m.createProperty("http://univ/enrolled_at");
    DTYPE = m.createProperty("http://univ/DTYPE");
    year = m.createProperty("http://univ/year");
    has_professor = m.createProperty("http://univ/has_professor");
    subject = m.createProperty("http://univ/subject");
    for_student = m.createProperty("http://univ/for_student");
    for_course = m.createProperty("http://univ/for_course");
    grade_value = m.createProperty("http://univ/grade_value");

  }

  public static Resource createInstance(Model m, Resource rdfClass, BaseEntity entity) {
    Resource instance = m.createResource(entity.getInstanceIRI());
    m.add(instance, rdfType, rdfClass);
    return instance;
  }

  public static Resource getInstance(Model m, Resource rdfClass, BaseEntity entity) {
    return getInstance(m, rdfClass.toString() + "/" + entity.getId());
  }

  public static Resource getInstance(Model m, String iri) {
    return m.getResource(iri);
  }
}
