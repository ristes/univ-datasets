package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.BaseEntity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

/**
 * @author Riste Stojanov
 */
public class Univ {

  public static Resource Faculty;
  public static Resource StudyProgram;
  public static Resource Subject;
  public static Resource Course;
  public static Resource User;
  public static Resource Grade;

  public static Property rdfLabel;
  public static Property rdfType;
  public static Property faculty;
  public static Property has_professor;
  public static Property for_student;
  public static Property has_course;
  public static Property grade_value;
  public static Property enrolled_at;
  public static Property works_at;


  public static void init(Model m) {
    Faculty = m.createResource("http://univ/Faculty");
    StudyProgram = m.createResource("http://univ/StudyProgram");
    Subject = m.createResource("http://univ/Subject");
    Course = m.createResource("http://univ/Course");
    User = m.createResource("http://univ/User");
    Grade = m.createResource("http://univ/Grade");

    rdfLabel = m.createProperty("http://www.w3.org/2000/01/rdf-schema#label");

    rdfType = m.createProperty("http://www.w3.org/2000/01/rdf-schema#type");
    faculty = m.createProperty("http://univ/faculty");
    has_professor = m.createProperty("http://univ/has_professor");

  }

  public static Resource createInstance(Model m, Resource rdfClass, BaseEntity entity) {
    Resource instance = m.createResource(rdfClass.toString() + "/" + entity.getId());
    m.add(instance, rdfType, rdfClass);
    return instance;
  }

  public static Resource getInstance(Model m, Resource rdfClass, BaseEntity entity) {
    return m.getResource(rdfClass.toString() + "/" + entity.getId());
  }
}
