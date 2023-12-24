package cz.uhk.sigmamail.model;

import java.util.List;

public interface UserDAO {
    User getUserById(int id);
    void deleteUser(User user);
    User getUserByUserame(String username);
    void updateUser(User user);
    List<User> getUsersByUsername(String username);
}
