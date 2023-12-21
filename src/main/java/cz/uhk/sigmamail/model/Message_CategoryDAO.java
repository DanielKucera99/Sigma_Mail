package cz.uhk.sigmamail.model;

import java.util.List;
public interface Message_CategoryDAO {

    Message_Category getMessageById(int id);
    Message_Category getCategoryById(int id);
    Message_Category getDataByMessageAndCategory(Message message, Category category);
    List<Message> getMessagesById(int id);
    List<Message> getMessageByCategory(Category category);
    public void moveToTrash(Message_Category currentMessage_Category, Message_Category newMessage_Category);
    public void deleteMessage_Category(Message_Category message_category);

}
