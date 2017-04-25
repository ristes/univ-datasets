package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.repository.StudyprogramRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;


/**
 * @author Riste Stojanov
 */
@Component
public class StudyProgramRepositoryImpl implements StudyprogramRepository {

  @Override
  public void save(StudyProgram studyProgram) {

    studyProgram.setId(StudyProgram.nextId());

    String instanceIri = studyProgram.getInstanceIRI();
    Model m = ModelHolder.createModel();

    Model defaultModel = ModelHolder.getDefaultModel();

    saveInstance(defaultModel, defaultModel, studyProgram);
    saveInstance(m, defaultModel, studyProgram);

    ModelHolder.createNamedModel(instanceIri, m);
  }

  private Resource saveInstance(Model m, Model defaultModel, StudyProgram entity) {
    Resource instance = Univ.createInstance(m, Univ.StudyProgramResource, entity);
    m.add(
      instance,
      Univ.rdfLabel,
      entity.getName()
    );

    m.add(
      instance,
      Univ.faculty,
      Univ.getInstance(defaultModel, Univ.FacultyResource, entity.getFaculty())
    );

    return instance;

  }

}
