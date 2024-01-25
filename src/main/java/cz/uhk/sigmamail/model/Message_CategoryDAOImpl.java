package cz.uhk.sigmamail.model;


import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class Message_CategoryDAOImpl implements Message_CategoryDAO {

    private final EntityManager entityManager;

    public Message_CategoryDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }


    @Override
    public Message_Category getDataByMessageAndCategory(Message message, Category category) {
        TypedQuery<Message_Category> query = entityManager.createQuery("SELECT mc FROM Message_Category mc WHERE mc.category = :category AND mc.message = :message", Message_Category.class);
        query.setParameter("category", category);
        query.setParameter("message", message);
        return query.getSingleResult();
    }

    @Override
    @Transactional
    public void deleteMessage_Category(Message_Category message_category) {
        entityManager.remove(message_category);
    }


}
