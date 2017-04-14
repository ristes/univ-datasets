package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.Faculty;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class FacultyFactory {

    private FacultyFactory(){}

    public static Faculty make() {
        Faculty faculty = new Faculty();
        faculty.setName(RandomStringUtils.randomAlphabetic(5,15));
        faculty.setNetworkAddress("10.10.10.10");
        return faculty;
    }

    public static List<Faculty> make(int n) {
        List<Faculty> faculties = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            faculties.add(FacultyFactory.make());
        }

        return faculties;
    }

}
