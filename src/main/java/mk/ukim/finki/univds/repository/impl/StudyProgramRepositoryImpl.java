package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.repository.StudyprogramRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;


/**
 * @author Riste Stojanov
 */
public class StudyProgramRepositoryImpl implements StudyprogramRepository {

  @Override
  public void save(StudyProgram studyProgram) {

    studyProgram.setId(StudyProgram.nextId());
    String instanceIri = String.format(
      StudyProgram.INSTANCE,
      studyProgram.getId()
    );
    Model m = ModelHolder.createNamedModel(instanceIri);

    Model defaultModel = ModelHolder.getDefaultModel();

    saveInstance(defaultModel, defaultModel, studyProgram);
    saveInstance(m, defaultModel, studyProgram);


  }

  private Resource saveInstance(Model m, Model defaultModel, StudyProgram entity) {
    Resource instance = Univ.createInstance(m, Univ.StudyProgram, entity);
    m.add(
      instance,
      Univ.rdfLabel,
      entity.getName()
    );

    m.add(
      instance,
      Univ.faculty,
      Univ.getInstance(defaultModel, Univ.Faculty, entity.getFaculty())
    );

    return instance;

  }

}
