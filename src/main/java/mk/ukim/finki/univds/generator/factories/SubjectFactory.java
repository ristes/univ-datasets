package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.Subject;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class SubjectFactory {

    private SubjectFactory(){};

    public static Subject make() {
        Subject subject = new Subject();
        subject.setName(RandomStringUtils.randomAlphabetic(5,15));
        subject.setDescription(RandomStringUtils.randomAlphabetic(10,30));
        return subject;
    }

    public static List<Subject> make (int n) {
        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            subjects.add(SubjectFactory.make());
        }
        return subjects;
    }

}
