package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.Subject;
import mk.ukim.finki.univds.repository.SubjectRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

/**
 * Implementation of {@link SubjectRepository}.
 */
@Component
public class SubjectRepositoryImpl implements SubjectRepository {

    @Override
    public void save(Subject subject) {
        subject.setId(Subject.nextId());

//        String instanceIri = subject.getInstanceIRI();
        Model defaultModel = ModelHolder.getDefaultModel();

        Resource instance = Univ.createInstance(defaultModel, Univ.SubjectResource, subject);

        defaultModel.add(instance, Univ.name, subject.getName());
        defaultModel.add(instance, Univ.description, subject.getDescription());

        subject.getStudyPrograms().forEach(sp -> {
            defaultModel.add(
                    instance,
                    Univ.study_program,
                    Univ.getInstance(defaultModel, Univ.StudyProgramResource, sp)
            );
        });
    }
}
