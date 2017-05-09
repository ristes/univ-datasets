package mk.ukim.finki.univds.repository.impl;

import mk.ukim.finki.univds.domain.Faculty;
import mk.ukim.finki.univds.repository.FacultyRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The faculty repository implementation.
 */
@Component
public class FacultyRepositoryImpl implements FacultyRepository {
    @Override
    public List<Faculty> findAll() {
        throw new NotImplementedException("@TODO: method implementation");
    }

    @Override
    public void save(List<Faculty> faculties) {
        faculties.forEach(this::save);
    }

    @Override
    public void save(Faculty faculty) {
        if (faculty.getId() == null) {
            faculty.setId(Faculty.nextId());
        }

//        String instanceIri = faculty.getInstanceIRI();
        Model m = ModelHolder.getDefaultModel();

        Resource instance = Univ.createInstance(m, Univ.FacultyResource, faculty);

        m.add(instance, Univ.name, faculty.getName());
        m.add(instance, Univ.networkAddress, faculty.getNetworkAddress());
    }
}
