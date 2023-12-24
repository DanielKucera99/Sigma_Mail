package cz.uhk.sigmamail.model;

import java.util.List;

public interface MessageDAO {

    Message getMessageById(int id);
    List<Message> getAllMessagesBySenderInCategory(User sender, Category category);
    List<Message> getAllMessagesByReceiverInCategory(User receiver, Category category);
    List<Message> getAllMessagesInTrash(User user);
    void sendMessage(User sender, User receiver, String subject, String text, List<Attachment> attachments);
    void saveMessage(User sender, User receiver,String subject,String text,List<Attachment> attachments);
    void associateMessageWithCategory(Message message, String categoryName);
    void associateMessageWithCategory(Message message, String categoryName, User user);
    void deleteMessage(Message message);
    void removeUser(User user);
}
