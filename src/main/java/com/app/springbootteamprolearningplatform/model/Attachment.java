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
@Entity(name = "attachments")
public class Attachment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false)
    UUID id;

    @Column(name = "content_type")
    String contentType;

    @Column(name = "file_name")
    String fileName;

    @OneToOne(mappedBy = "attachment")
    Task task;

    @OneToOne(mappedBy = "attachment")
    User user;

    @OneToOne(mappedBy = "attachment")
    Course course;

    @Column(nullable = false)
    byte[] bytes;
}
