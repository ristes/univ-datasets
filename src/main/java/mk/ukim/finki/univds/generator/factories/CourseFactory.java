package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.Course;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class CourseFactory {

    private CourseFactory(){};

    public static Course make() {
        Course course = new Course();
        course.setYear("201"+RandomStringUtils.randomNumeric(1));
        return course;
    }

    public static List<Course> make (int n) {
        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            courses.add(CourseFactory.make());
        }
        return courses;
    }

}
