package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.Grade;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class GradeFactory {

    private GradeFactory(){};

    public static Grade make() {
        Grade grade = new Grade();
        grade.setGrade(RandomUtils.nextInt(6,11));
        return grade;
    }

    public static List<Grade> make (int n) {
        List<Grade> grades = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            grades.add(GradeFactory.make());
        }
        return grades;
    }
}
