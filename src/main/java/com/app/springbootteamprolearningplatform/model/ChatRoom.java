package com.app.springbootteamprolearningplatform.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "chat_rooms")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    User user2;

    @OneToMany(mappedBy = "chatRoom")
    List<Message> messages;

    public ChatRoom(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    // TO DO TO BE DTO
    @Transient
    String guestUserImg;

    @Transient
    String myImage;

    @Transient
    Long newMessagesCount;
}
