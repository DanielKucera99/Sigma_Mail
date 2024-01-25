package cz.uhk.sigmamail.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{

    private final EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public User getUserById(int id){
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        entityManager.remove(user);
    }

    @Override
    public User getUserByUserame(String username) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE username=:username", User.class);
        query.setParameter("username",username);

        List<User> resultList = query.getResultList();
        if (!resultList.isEmpty()) {
            return resultList.getFirst();
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.username LIKE :username", User.class);
        query.setParameter("username", "%" + username + "%");

        return query.getResultList();
    }


}

