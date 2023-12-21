package cz.uhk.sigmamail.model;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Message_CategoryDAOImpl implements Message_CategoryDAO {

    private EntityManager entityManager;

    public Message_CategoryDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Message_Category getCategoryById(int id){
        return entityManager.find(Message_Category.class, id);
    }

    @Override
    public Message_Category getMessageById(int id){
        return entityManager.find(Message_Category.class, id);
    }

    public List<Message> getMessageByCategory(Category category) {
        TypedQuery<Message> query = entityManager.createQuery("SELECT message FROM Message_Category WHERE category = :category", Message.class);
        query.setParameter("category", category);
        return query.getResultList();
    }
}
