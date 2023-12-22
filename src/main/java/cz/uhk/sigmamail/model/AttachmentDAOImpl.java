package cz.uhk.sigmamail.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Repository
public class AttachmentDAOImpl implements AttachmentDAO {

    private EntityManager entityManager;

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
