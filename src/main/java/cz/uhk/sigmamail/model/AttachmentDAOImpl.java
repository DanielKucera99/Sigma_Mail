package cz.uhk.sigmamail.model;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    private final EntityManager entityManager;

    @Autowired
    public AttachmentDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Attachment getAttachmentById(int id) {
        return entityManager.find(Attachment.class, id);
    }

    @Override
    @Transactional
    public void deleteAttachment(Attachment attachment) {
        entityManager.remove(attachment);
    }

    @Override
    @Transactional
    public void deleteAttachments(List<Attachment> attachments) {
        for(Attachment attachment : attachments){
            entityManager.remove(attachment);
        }
    }

}
