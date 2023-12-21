package cz.uhk.sigmamail.model;

import java.util.List;
public interface Message_CategoryDAO {

    Message_Category getMessageById(int id);
    Message_Category getCategoryById(int id);
    List<Message> getMessageByCategory(Category category);

}
