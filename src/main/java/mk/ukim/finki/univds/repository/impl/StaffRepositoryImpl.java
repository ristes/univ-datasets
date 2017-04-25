package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.User;
import mk.ukim.finki.univds.generator.factories.UserFactory;
import mk.ukim.finki.univds.repository.UserRepository;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Repository that saves
 * {@link UserFactory#PROFESSOR_TYPE}
 * {@link UserFactory#TECHNICAL_STAFF_TYPE}
 * {@link UserFactory#SUPER_ADMINISTRATOR_TYPE}.
 */
@Component("staffRepository")
public class StaffRepositoryImpl implements UserRepository {

    @Override
    public void save(User staff) {
        staff.setId(User.nextId());

//        String instanceIri = staff.getInstanceIRI();
        Model defaultModel = ModelHolder.getDefaultModel();

        Resource instance = Univ.createInstance(defaultModel, Univ.UserResource, staff);

        defaultModel.add(instance, Univ.DTYPE, staff.getDTYPE());
        defaultModel.add(instance, Univ.name, staff.getName());
        defaultModel.add(instance, Univ.email, staff.getEmail());
        defaultModel.add(instance, Univ.phone, staff.getPhone());
        defaultModel.add(
                instance,
                Univ.works_at,
                Univ.getInstance(defaultModel, Univ.FacultyResource, staff.getFaculty())
        );
    }

    @Override
    public void save(List<User> staff) {
        staff.forEach(this::save);
    }
}
