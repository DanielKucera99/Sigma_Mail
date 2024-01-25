package cz.uhk.sigmamail.model;

import jakarta.persistence.*;

@Entity
@Table(name = "message_category")
public class Message_Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "message")
    private Message message;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    @OneToOne
    @JoinColumn(name="moved_by")
    private User moved_by;

    public User getMoved_by() {
        return moved_by;
    }

    public void setMoved_by(User moved_by) {
        this.moved_by = moved_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


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
                "id=" + id +
                ", message=" + message +
                ", category=" + category +
                ", moved_by=" + moved_by +
                '}';
    }
}
