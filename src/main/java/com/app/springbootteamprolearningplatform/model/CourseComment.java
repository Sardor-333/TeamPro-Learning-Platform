package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "course_comments")
public class CourseComment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String body;

    @Column(name = "posted_at")
    LocalDateTime postedAt;

    public CourseComment(Course course, User user, String body, LocalDateTime postedAt) {
        this.course = course;
        this.user = user;
        this.body = body;
        this.postedAt = postedAt;
    }

    public String getBase64() {
        if (this.user.getAttachment() != null) {
            byte[] bytes = Base64.getEncoder().encode(user.getAttachment().getBytes());
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }
}
