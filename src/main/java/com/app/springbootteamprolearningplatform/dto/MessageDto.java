package com.app.springbootteamprolearningplatform.dto;

import com.app.springbootteamprolearningplatform.model.ChatRoom;
import com.app.springbootteamprolearningplatform.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    UUID id;
    String message;
    String sentAt;
    Boolean isRead;
    User from;
    ChatRoom chatRoom;

}
