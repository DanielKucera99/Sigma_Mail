package cz.uhk.sigmamail.model;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
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
    public Message_Category getDataByMessageAndCategory(Message message, Category category) {
        TypedQuery<Message_Category> query = entityManager.createQuery("SELECT mc FROM Message_Category mc WHERE mc.category = :category AND mc.message = :message", Message_Category.class);
        query.setParameter("category", category);
        query.setParameter("message", message);
        return query.getSingleResult();
    }

    @Override
    public List<Message> getMessagesById(int id) {
        return null;
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

    @Override
    @Transactional
    public void moveToTrash(Message_Category currentMessage_Category, Message_Category newMessage_Category) {
        entityManager.remove(currentMessage_Category);
        entityManager.persist(newMessage_Category);
    }

    @Override
    @Transactional
    public void deleteMessage_Category(Message_Category message_category) {
        entityManager.remove(message_category);
    }


}
