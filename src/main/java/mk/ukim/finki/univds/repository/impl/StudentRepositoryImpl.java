package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.domain.User;
import mk.ukim.finki.univds.repository.UserRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Riste Stojanov
 */
@Component("studentRepository")
public class StudentRepositoryImpl implements UserRepository {

  @Override
  public void save(List<User> students) {
    students.forEach(this::save);
  }

  @Override
  public void save(User student) {
    student.setId(User.nextId());

//        String instanceIri = student.getInstanceIRI();
    Model defaultModel = ModelHolder.getDefaultModel();
    Model studyProgramNamedGraph = ModelHolder.getNamedGraph(student.getStudyProgram().getInstanceIRI());

    saveInstance(defaultModel, defaultModel, student);
    saveInstance(studyProgramNamedGraph, defaultModel, student);

  }

  private Resource saveInstance(Model m, Model defaultModel, User student) {
    Resource instance = Univ.createInstance(defaultModel, Univ.UserResource, student);

    m.add(instance, Univ.DTYPE, student.getDTYPE());
    m.add(instance, Univ.name, student.getName());
    m.add(instance, Univ.email, student.getEmail());
    m.add(instance, Univ.phone, student.getPhone());
    m.add(
            instance,
            Univ.enrolled_at,
            Univ.getInstance(defaultModel, Univ.StudyProgramResource, student.getStudyProgram())
    );

    return instance;

  }
}
