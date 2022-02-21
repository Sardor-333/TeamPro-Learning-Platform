package com.app.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "modules")
public class Module {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(nullable = false)
    String name;

    Double price;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    Course course;

    @OneToMany(mappedBy = "module")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    List<Lesson> lessons;

    public Module(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
