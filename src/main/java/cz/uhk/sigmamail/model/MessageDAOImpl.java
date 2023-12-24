package cz.uhk.sigmamail.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class MessageDAOImpl implements MessageDAO{

    private final EntityManager entityManager;

    public MessageDAOImpl(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    @Override
    public Message getMessageById(int id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> getAllMessagesBySenderInCategory(User sender, Category category) {
        TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m " +
                "JOIN Message_Category mc ON m.id = mc.message.id " +
                "WHERE mc.category = :category " +
                "AND m.sender = :sender", Message.class);
        query.setParameter("sender", sender);
        query.setParameter("category", category);
        return query.getResultList();
    }

    @Override
    public List<Message> getAllMessagesByReceiverInCategory(User receiver, Category category) {
        TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m " +
                "JOIN Message_Category mc ON m.id = mc.message.id " +
                "WHERE mc.category = :category " +
                "AND m.receiver = :receiver", Message.class);
        query.setParameter("receiver", receiver);
        query.setParameter("category", category);
        return query.getResultList();
    }
    @Override
    public List<Message> getAllMessagesInTrash(User user){
        TypedQuery<Message> query = entityManager.createQuery("SELECT m FROM Message m " +
                "JOIN Message_Category mc ON m.id = mc.message.id " +
                "WHERE mc.moved_by = :user", Message.class);
        query.setParameter("user", user);

        return query.getResultList();
    }

    @Override
    @Transactional
    public void sendMessage(User sender, User receiver, String subject, String text, List<Attachment> attachments) {
        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setSubject(subject);
        newMessage.setText(text);


        if(attachments != null) {
            for (Attachment attachment : attachments) {
                attachment.setMessage(newMessage);
            }

            newMessage.setAttachments(attachments);
        }
        newMessage.setTime(new Date());

        entityManager.persist(newMessage);

        if(attachments != null) {
            for (Attachment attachment : attachments) {
                entityManager.persist(attachment);
            }
        }
        associateMessageWithCategory(newMessage, "Sent");
        associateMessageWithCategory(newMessage, "Received");
    }

    @Override
    @Transactional
    public void saveMessage(User sender, User receiver, String subject, String text, List<Attachment> attachments) {
        Message newMessage = new Message();
        newMessage.setSender(sender);
        newMessage.setReceiver(receiver);
        newMessage.setSubject(subject);
        newMessage.setText(text);

        if(attachments != null) {
            for (Attachment attachment : attachments) {
                attachment.setMessage(newMessage);
            }

            newMessage.setAttachments(attachments);
        }
        newMessage.setTime(new Date());

        entityManager.persist(newMessage);

        if(attachments != null) {
            for (Attachment attachment : attachments) {
                entityManager.persist(attachment);
            }
        }
        associateMessageWithCategory(newMessage, "Concepts");
    }

    @Override
    @Transactional
    public void associateMessageWithCategory(Message message, String categoryName) {
        TypedQuery<Category> categoryQuery = entityManager.createQuery(
                "SELECT c FROM Category c WHERE c.name = :categoryName", Category.class);
        categoryQuery.setParameter("categoryName", categoryName);

        Category category = categoryQuery.getSingleResult();

        Message_Category messageCategory = new Message_Category();
        messageCategory.setMessage(message);
        messageCategory.setCategory(category);
        entityManager.persist(messageCategory);
    }
    @Override
    @Transactional
    public void associateMessageWithCategory(Message message, String categoryName, User user) {
        TypedQuery<Category> categoryQuery = entityManager.createQuery(
                "SELECT c FROM Category c WHERE c.name = :categoryName", Category.class);
        categoryQuery.setParameter("categoryName", categoryName);

        Category category = categoryQuery.getSingleResult();

        Message_Category messageCategory = new Message_Category();
        messageCategory.setMessage(message);
        messageCategory.setCategory(category);
        messageCategory.setMoved_by(user);
        entityManager.persist(messageCategory);
    }

    @Override
    @Transactional
    public void deleteMessage(Message message) {
        entityManager.remove(message);
    }

    @Override
    @Transactional
    public void removeUser(User user) {
        TypedQuery<Message> senderQuery = entityManager.createQuery("SELECT m FROM Message m WHERE m.sender = :user", Message.class);
        senderQuery.setParameter("user", user);
        List<Message> senderResults = senderQuery.getResultList();
        for(Message message: senderResults)
        {
            message.setSender(null);
            entityManager.merge(message);
        }
        TypedQuery<Message> receiverQuery = entityManager.createQuery("SELECT m FROM Message m WHERE m.receiver = :user", Message.class);
        receiverQuery.setParameter("user", user);
        List<Message> receiverResults = receiverQuery.getResultList();
        for(Message message: receiverResults)
        {
            message.setReceiver(null);
            entityManager.merge(message);
        }


    }


}
