package cz.uhk.sigmamail.model;

import java.util.List;

public interface MessageDAO {

    public Message getMessageById(int id);
    public List<Message> getAllMessages();

   public List<Message> getAllMessagesBySenderInCategory(User sender, Category category);
    public List<Message> getAllMessagesByReceiverInCategory(User receiver, Category category);
    public void sendMessage(User sender, User receiver, String subject, String text, List<Attachment> attachments);
    public void sendMessage(Message message);
}
