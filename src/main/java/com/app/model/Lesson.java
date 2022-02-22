package com.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false)
    String theme;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    Module module;

    @OneToMany(mappedBy = "lesson")
    List<Task> tasks;

    @OneToMany(mappedBy = "lesson")
    List<LessonReview> reviews;

    @OneToMany(mappedBy = "lesson")
    List<Video> videos;

    public Lesson(String theme) {
        this.theme = theme;
    }


}
