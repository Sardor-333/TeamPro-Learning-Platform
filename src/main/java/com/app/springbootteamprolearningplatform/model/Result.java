package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "results")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    UUID id;

    @OneToOne
    @JoinColumn(name = "submission_id", nullable = false, referencedColumnName = "id")
    Submission submission;

    @Column(name = "is_correct", nullable = false)
    Boolean isCorrect;

    @Column(nullable = false)
    Double ball;
}
