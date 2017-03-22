package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.StudyProgram;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class StudyprogramFactory {

    private StudyprogramFactory(){};

    public static StudyProgram make() {
        StudyProgram studyprogram = new StudyProgram();
        studyprogram.setName(RandomStringUtils.randomAlphabetic(5,15));
        return studyprogram;
    }

    public static List<StudyProgram> make (int n) {
        List<StudyProgram> courses = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            courses.add(StudyprogramFactory.make());
        }
        return courses;
    }
}
