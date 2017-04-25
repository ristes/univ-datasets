package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.Course;
import mk.ukim.finki.univds.repository.CourseRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link CourseRepository}.
 */
@Component
public class CourseRepositoryImpl implements CourseRepository {

    @Override
    public void save(Course course) {
        course.setId(Course.nextId());

//        String instanceIri = course.getInstanceIRI();
        Model defaultModel = ModelHolder.getDefaultModel();

        Resource instance = Univ.createInstance(defaultModel, Univ.CourseResource, course);

        defaultModel.add(instance, Univ.year, course.getYear());
        defaultModel.add(
                instance,
                Univ.has_professor,
                Univ.getInstance(defaultModel, Univ.UserResource, course.getProfessor())
        );
        defaultModel.add(
                instance,
                Univ.subject,
                Univ.getInstance(defaultModel, Univ.SubjectResource, course.getSubject())
        );
        course.getStudents().forEach(student -> {
            defaultModel.add(
                    instance,
                    Univ.for_student,
                    Univ.getInstance(defaultModel, Univ.UserResource, student)
            );
        });

    }
}
