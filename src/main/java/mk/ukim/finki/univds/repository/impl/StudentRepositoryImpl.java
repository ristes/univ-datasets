package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.StudyProgram;
import mk.ukim.finki.univds.domain.User;
import mk.ukim.finki.univds.repository.UserRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;

import java.util.List;

/**
 * @author Riste Stojanov
 */
public class StudentRepositoryImpl implements UserRepository {

  @Override
  public void save(User student) {
    student.setId(User.nextStudentId());

    StudyProgram sp = student.getStudyProgram();

    Model m = ModelHolder.getDataSource().getNamedModel(Univ.StudyProgram + "/" + sp.getId());

    Resource instance = Univ.createInstance(m, Univ.User, student);

    m.add(
      instance,
      Univ.enrolled_at,
      Univ.getInstance(m, Univ.StudyProgram, student.getStudyProgram())
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

  @Override
  public void save(List<User> allUsers) {
    for (User u : allUsers) {
      save(u);
    }
  }
}
