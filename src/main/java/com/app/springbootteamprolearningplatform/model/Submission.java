package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    Task task;

    @Column(nullable = false)
    String answer;

    @Column(name = "submitted_time", nullable = false)
    LocalDateTime submittedTime;

    public Submission(User user, Task task, LocalDateTime submittedTime) {
        this.user = user;
        this.task = task;
        this.submittedTime = submittedTime;
    }
}
