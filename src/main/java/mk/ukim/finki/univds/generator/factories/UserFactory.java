package mk.ukim.finki.univds.generator.factories;

import mk.ukim.finki.univds.domain.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public class UserFactory {

  public static final String PROFESSOR_TYPE = "Professor";
  public static final String STUDENT_TYPE = "Student";

  private UserFactory() {
  }

  public static User make(String userDType) {
    User user = new User();
    user.setName(RandomStringUtils.randomAlphabetic(5, 15));
    user.setEmail(user.getName().concat("@email.com"));
    user.setDTYPE(userDType);
    user.setPhone("070".concat(RandomStringUtils.randomNumeric(6)));

    return user;
  }

  public static List<User> make(int n, String userDType) {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      users.add(UserFactory.make(userDType));
    }
    return users;
  }

}
