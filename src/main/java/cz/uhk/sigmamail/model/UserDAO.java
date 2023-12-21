package cz.uhk.sigmamail.model;

import java.util.List;

public interface UserDAO {
    User getUserById(int id);
    void saveUser(User user);
    void deleteUser(int id);
    List<User> getAllUsers();
    User getUserByUserame(String username);
}
