package mk.ukim.finki.univds.repository;

import mk.ukim.finki.univds.domain.User;

import java.util.List;

/**
 * Created by KarateKid on 04.2.2017.
 */
public interface UserRepository {
  void save(User superAdministrator);

  void save(List<User> technicalStaff);
}
