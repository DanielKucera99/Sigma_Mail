package cz.uhk.sigmamail.model;


public interface Message_CategoryDAO {

    Message_Category getDataByMessageAndCategory(Message message, Category category);
    void deleteMessage_Category(Message_Category message_category);

}
