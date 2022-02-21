package com.app.model;

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
    @Column(updatable = false, nullable = false)
    UUID id;

    @Column(name = "file_location", nullable = false)
    String fileLocation;

    @Column(name = "file_name", nullable = false)
    String fileName;

    @OneToOne(mappedBy = "attachment")
    Task task;

    @OneToOne(mappedBy = "attachment")
    User user;


    @Column(nullable = false)
    byte[] bytes;
}
