package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.Grade;
import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.repository.GradeRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

/**
 * @author Riste Stojanov
 */
public class GradeRepositoryImpl implements GradeRepository {

  @Override
  public void save(Grade grade) {

    grade.setId(Grade.nextId());

    StudyProgram sp = grade.getStudent().getStudyProgram();

    Model m = ModelHolder.getDataSource().getNamedModel(Univ.StudyProgram + "/" + sp.getId());

    Resource instance = Univ.createInstance(m, Univ.Grade, grade);

    m.add(
      instance,
      Univ.for_student,
      Univ.getInstance(m, Univ.User, grade.getStudent())
    );


    m.add(
      instance,
      Univ.has_course,
      Univ.getInstance(m, Univ.Course, grade.getCourse())
    );


    m.add(
      instance,
      Univ.grade_value,
      grade.getGrade() + ""
    );
  }
}
