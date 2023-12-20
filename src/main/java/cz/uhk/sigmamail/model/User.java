package cz.uhk.sigmamail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Required")
    @Email
    @Column(name = "username")
    private String username;

    @NotNull(message = "Required")
    @Size(min = 1, message = "Required")
    @Column(name = "first_name")
    private String first_name;

    @NotNull(message = "Required")
    @Size(min = 1, message = "Required")
    @Column(name = "last_name")
    private String last_name;

    @NotNull(message = "Required")
    @Temporal(TemporalType.DATE)
    @Column(name = "birthdate")
    private Date birthdate;

    @NotNull(message = "Required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    @NotNull(message = "Required")
    @Size(min = 1, message = "Required")
    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "sender")
    private List<Message> messages;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", birthdate=" + birthdate +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
