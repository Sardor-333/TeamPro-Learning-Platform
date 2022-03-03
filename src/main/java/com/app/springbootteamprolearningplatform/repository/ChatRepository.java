package com.app.springbootteamprolearningplatform.repository;

import com.app.springbootteamprolearningplatform.model.ChatRoom;
import com.app.springbootteamprolearningplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<ChatRoom, UUID> {

    List<ChatRoom> findAllByUser1OrUser2(User user1, User user2);

    boolean existsByUser1IdOrUser2Id(UUID user1Id, UUID user2Id);
}
