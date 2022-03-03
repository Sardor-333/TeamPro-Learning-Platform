package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    UUID id;

    @NotBlank
    String message;

    @Column(name = "sent_at")
    LocalDateTime sentAt;

    @ColumnDefault(value = "false")
    Boolean isRead;

    @ManyToOne
    @JoinColumn(name = "from_id", nullable = false)
    User from;

    @ManyToOne
    @JoinColumn(name = "chat_room_id", nullable = false)
    ChatRoom chatRoom;

    public Message(String message, Boolean isRead, User from, ChatRoom chatRoom, LocalDateTime sentAt) {
        this.message = message;
        this.isRead = isRead;
        this.from = from;
        this.chatRoom = chatRoom;
        this.sentAt = sentAt;
    }
}
