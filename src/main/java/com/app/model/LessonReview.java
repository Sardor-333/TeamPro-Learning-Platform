package com.app.springbootteamprolearningplatform.model;

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
@Entity(name = "lesson_comments")
public class LessonComment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @JoinColumn(name = "lesson_id", nullable = false)
    @ManyToOne
    Lesson lesson;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false)
    String body;

    @Column(name = "posted_at")
    LocalDateTime postedAt;

    public LessonComment(Lesson lesson, User user, String body, LocalDateTime postedAt) {
        this.lesson = lesson;
        this.user = user;
        this.body = body;
        this.postedAt = postedAt;
    }
}
