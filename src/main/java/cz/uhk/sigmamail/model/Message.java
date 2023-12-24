package cz.uhk.sigmamail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver")
    private User receiver;

    @Column(name = "subject")
    private String subject;

    @Size(max = 512)
    @Column(name = "text")
    private String text;

    @OneToMany(mappedBy = "message", cascade = CascadeType.REMOVE)
    private List<Attachment> attachments;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date time;

    @ManyToMany
    @JoinTable(
            name="message_category",
            joinColumns = @JoinColumn(name="message"),
            inverseJoinColumns = @JoinColumn(name="category")
    )
    private Set<Category> categories;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + (sender != null ? sender.getId() : null) + // Include sender's ID instead of the whole object
                ", receiver=" + (receiver != null ? receiver.getId() : null) + // Include receiver's ID instead of the whole object
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}