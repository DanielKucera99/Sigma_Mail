package cz.uhk.sigmamail.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message_category")
public class Message_Category {

    @Id
    @ManyToOne
    @JoinColumn(name = "message")
    private Message message;

    @Id
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Message_Category{" +
                "message=" + message +
                ", category=" + category +
                '}';
    }
}
