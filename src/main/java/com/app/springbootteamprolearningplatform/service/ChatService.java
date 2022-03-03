package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.model.ChatRoom;
import com.app.springbootteamprolearningplatform.model.Message;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.ChatRepository;
import com.app.springbootteamprolearningplatform.repository.MessageRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import com.app.springbootteamprolearningplatform.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ChatService(ChatRepository chatRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public boolean createChat(UUID hostId, UUID guestId) {
        if (chatRepository.existsByUser1IdOrUser2Id(hostId, guestId) || chatRepository.existsByUser1IdOrUser2Id(guestId, hostId)) {
            return false;
        }

        User host = userRepository.findById(hostId).orElse(null);
        User guest = userRepository.findById(guestId).orElse(null);

        if (host == null || guest == null || host.equals(guest)) {
            return false;
        }

        ChatRoom chatRoom = new ChatRoom(host, guest);
        chatRepository.save(chatRoom);
        return true;
    }

    public List<ChatRoom> getUserChats(UUID userId) {
        User sender = userRepository.findById(userId).orElse(null);
        List<ChatRoom> userChats = chatRepository.findAllByUser1OrUser2(sender, sender);
        for (ChatRoom chat : userChats) {
            User user1 = chat.getUser1();
            if (user1.equals(sender)) {
                chat.setGuestUserImg(chat.getUser2().getBase64());
                chat.setMyImage(user1.getBase64());
                continue;
            }
            chat.setGuestUserImg(user1.getBase64());
            chat.setMyImage(sender.getBase64());
        }
        return userChats;
    }

    public List<Message> findChatMessages(UUID chatId) {
        return messageRepository.findAllByChatRoomIdOrderBySentAtAsc(chatId);
    }

    public boolean sendMessage(HttpServletRequest request, UUID chatId, String messageBody) {
        if (messageBody == null || !messageBody.isBlank()) {
            return false;
        }

        ChatRoom chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) return false;

        User sender = userRepository.findById(UUID.fromString(request.getSession().getAttribute("userId").toString())).orElse(null);
        if (!chat.getUser1().equals(sender) && !chat.getUser2().equals(sender)) return false;

        Message message = new Message(messageBody, false, sender, chat, LocalDateTime.now());
        messageRepository.save(message);
        return true;
    }

    public User searchUserForHost(UUID hostId, String email) {
        if (hostId == null || !userRepository.existsById(hostId) || !Util.isValidEmail(email)) return null;

        User host = userRepository.findById(hostId).orElse(null);
        User wanted = userRepository.findByEmail(email).orElse(null);

        if (Objects.requireNonNull(host).equals(wanted)) return null;
        return wanted;
    }
}
