package cz.uhk.sigmamail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;


@Entity
@Table(name="attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="message", nullable = false)
    private Message message;

    @NotNull(message="Required")
    @Size(min=1)
    @Column(name="name")
    private String name;

    @NotNull(message="Required")
    @Size(min=1)
    @Column(name="type")
    private String type;

    @NotNull(message="Required")
    @Size(min=1)
    @Column(name="size")
    private int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id=" + id +
                ", message=" + message +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                '}';
    }
}