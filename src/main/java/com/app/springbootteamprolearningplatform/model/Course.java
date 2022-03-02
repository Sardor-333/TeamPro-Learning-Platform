package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "courses")
public class Course {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;

    @OneToMany(mappedBy = "course")
    List<Module> modules;

    @OneToMany(mappedBy = "course")
    List<CourseComment> comments;

    @OneToMany(mappedBy = "course")
    List<CourseVote> votes;

    @ManyToMany(mappedBy = "courses")
    List<User> authors;

    @OneToOne(cascade = CascadeType.ALL) // todo to test
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    Attachment attachment;

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getBase64Encode() {
        try {
            if (this.attachment != null) {
                byte[] encode = Base64.getEncoder().encode(this.attachment.getBytes());
                return new String(encode, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
