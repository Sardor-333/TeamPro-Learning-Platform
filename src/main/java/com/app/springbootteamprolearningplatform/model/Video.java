package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "videos")
public class Video {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(name = "file_location", nullable = false)
    String fileLocation;

    @Column(name = "original_file_name")
    String originalFileName;

    @Column(name = "file_name", nullable = false)
    String fileName;

    @Column(name = "content_type")
    String contentType;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    Lesson lesson;

    public Video(String fileLocation, String fileName) {
        this.fileLocation = fileLocation;
        this.fileName = fileName;
    }
}
