package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.Grade;
import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.repository.GradeRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

/**
 * @author Riste Stojanov
 */
@Component
public class GradeRepositoryImpl implements GradeRepository {

  @Override
  public void save(Grade grade) {

    grade.setId(Grade.nextId());

    StudyProgram sp = grade.getStudent().getStudyProgram();

    Model defaultModel = ModelHolder.getDefaultModel();
    Model studyProgramNamedGraph = ModelHolder.getNamedGraph(sp.getInstanceIRI());

    saveInstance(defaultModel, defaultModel, grade);
    saveInstance(studyProgramNamedGraph, defaultModel, grade);

  }

  private Resource saveInstance(Model m, Model defaultModel, Grade grade) {
    Resource instance = Univ.createInstance(m, Univ.GradeResource, grade);

    m.add(
            instance,
            Univ.for_course,
            Univ.getInstance(defaultModel, Univ.CourseResource, grade.getCourse())
    );
    m.add(
            instance,
            Univ.for_student,
            Univ.getInstance(defaultModel, Univ.UserResource, grade.getStudent())
    );

    m.add(instance, Univ.grade_value, grade.getGrade().toString());


    return instance;

  }
}
