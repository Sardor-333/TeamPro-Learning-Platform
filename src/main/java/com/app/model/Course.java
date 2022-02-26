package com.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
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
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    List<Module> modules;

    @OneToMany(mappedBy = "course")
    List<CourseReview> reviews;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    List<CourseVote> votes;

    @ManyToMany
    @JoinTable(
            name = "courses_authors",
            joinColumns = {@JoinColumn(name = "course_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    List<User> authors;

    @OneToOne
    @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    Attachment attachment;

    public Course(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
