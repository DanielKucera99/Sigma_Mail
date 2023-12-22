package cz.uhk.sigmamail.model;

import java.util.List;
public interface AttachmentDAO {

    public Attachment getAttachmentById(int id);

    public void deleteAttachment(Attachment attachment);

    public void deleteAttachments(List<Attachment> attachments);
}
