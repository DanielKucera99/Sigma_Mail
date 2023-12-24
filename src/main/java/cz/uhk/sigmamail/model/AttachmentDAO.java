package cz.uhk.sigmamail.model;

import java.util.List;
public interface AttachmentDAO {

    Attachment getAttachmentById(int id);

    void deleteAttachment(Attachment attachment);

    void deleteAttachments(List<Attachment> attachments);
}
