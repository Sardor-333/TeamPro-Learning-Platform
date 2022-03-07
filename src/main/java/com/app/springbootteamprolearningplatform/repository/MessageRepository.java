package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.Message;
import com.app.springbootteamprolearningplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findAllByChatRoomIdOrderBySentAtAsc(UUID chatRoomId);

    List<Message> findAllByChatRoomId(UUID chatId);

    Long countAllByChatRoomIdAndFromAndIsRead(UUID chatRoomId, User from, Boolean isRead);

}
