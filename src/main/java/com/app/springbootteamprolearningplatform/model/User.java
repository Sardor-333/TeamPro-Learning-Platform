package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @Size(min = 2, message = "First name should contain at least 2 characters!")
    @NotBlank(message = "First name should contain characters!")
    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    @NotBlank(message = "Password should contain characters!")
    @Size(min = 6, message = "Password length must be at least 6!")
    String password;

    String bio;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "users_courses", joinColumns = {
            @JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    List<Course> userCourses;

    @ManyToMany(mappedBy = "authors", cascade = CascadeType.ALL)
    List<Course> courses;

    Double balance;

    // RELATIONS
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    Attachment attachment;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    List<LessonComment> lessonComments;

    @OneToMany(mappedBy = "user")
    List<CourseComment> courseComments;

    @OneToMany(mappedBy = "user")
    List<CourseRate> courseVotes;

    @OneToMany(mappedBy = "user")
    List<Submission> submissions;

    // Chat fields
    @OneToMany(mappedBy = "user1")
    List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user2")
    List<ChatRoom> chatRooms1;

    // message fields
    @OneToMany(mappedBy = "from")
    List<Message> messages;

    public User(String firstName, String lastName, String email, String password, String bio, Attachment attachment, Double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.attachment = attachment;
        this.balance = balance;
    }

    public String getBase64() {
        if (attachment != null) {
            byte[] bytes = Base64.getEncoder().encode(attachment.getBytes());
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }
}
