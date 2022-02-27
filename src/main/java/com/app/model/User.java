package com.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
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

    @Column(name = "first_name", nullable = false)
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String password;

    String bio;

    @OneToOne
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
    List<LessonReview> lessonReviews;

    @OneToMany(mappedBy = "user")
    List<CourseReview> courseReviews;

    @OneToMany(mappedBy = "user")
    List<CourseVote> courseVotes;

    @ManyToMany(mappedBy = "authors")
    List<Course> courses;

    public User(String firstName, String lastName, String email, String password, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.bio = bio;
    }

    public String getEncode64() {
        try {
            if (attachment != null) {
                byte[] encode = Base64.getEncoder().encode(attachment.getBytes());
                return new String(encode, "UTF-8");
            }
            return null;
        } catch (UnsupportedEncodingException e) {
            System.out.println("Exception: " + e.getMessage());
            return null;
        }
    }
}
