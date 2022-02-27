package com.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "course_reviews")
public class CourseReview {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String body;

    @Column(name = "posted_at")
    LocalDateTime postedAt;

    public CourseReview(Course course, User user, String body) {
        this.course = course;
        this.user = user;
        this.body = body;
    }

    public CourseReview(Course course, User user, String body, LocalDateTime postedAt) {
        this.course = course;
        this.user = user;
        this.body = body;
        this.postedAt = postedAt;
    }
}
