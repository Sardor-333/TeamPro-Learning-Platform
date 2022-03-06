package com.app.springbootteamprolearningplatform.service;

import com.app.springbootteamprolearningplatform.dto.MessageDto;
import com.app.springbootteamprolearningplatform.model.ChatRoom;
import com.app.springbootteamprolearningplatform.model.Message;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.ChatRepository;
import com.app.springbootteamprolearningplatform.repository.MessageRepository;
import com.app.springbootteamprolearningplatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public ChatService(ChatRepository chatRepository,
                       UserRepository userRepository,
                       MessageRepository messageRepository,
                       UserService userService) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public ChatRoom createChat(UUID hostId, UUID guestId) {
        if (chatRepository.existsByUser1IdAndUser2Id(hostId, guestId) || chatRepository.existsByUser1IdAndUser2Id(guestId, hostId)) {
            ChatRoom chatRoom = chatRepository.findByUser1IdAndUser2Id(hostId, guestId);
            if (chatRoom==null) {
                chatRoom = chatRepository.findByUser1IdAndUser2Id(guestId,hostId);
            }
            return chatRoom;
        }

        User host = userRepository.findById(hostId).orElse(null);
        User guest = userRepository.findById(guestId).orElse(null);

        if (host == null || guest == null || host.equals(guest))
            return null;

        ChatRoom chatRoom = new ChatRoom(host, guest);
        ChatRoom save = chatRepository.save(chatRoom);
        return save;
    }

    public List<ChatRoom> getUserChats(UUID userId) {
        User sender = userRepository.findById(userId).orElse(null);
        List<ChatRoom> userChats = chatRepository.findAllByUser1OrUser2(sender, sender);
        for (ChatRoom chat : userChats) {
            User user2 = chat.getUser2();
            User user11 = chat.getUser1();
            if (user11.getId().equals(userId)) {
                chat.setUser1(user11);
                chat.setUser2(user2);
            } else {
                chat.setUser1(user2);
                chat.setUser2(user11);
            }
            User user1 = chat.getUser1();
            if (user1.equals(sender)) {
                chat.setGuestUserImg(chat.getUser2().getBase64());
                chat.setMyImage(sender.getBase64());
                chat.setNewMessagesCount(messageRepository.countAllByChatRoomIdAndFromAndIsRead(chat.getId(), chat.getUser2(), false));
            } else {
                chat.setGuestUserImg(user1.getBase64());
                chat.setMyImage(Objects.requireNonNull(sender).getBase64());
                chat.setNewMessagesCount(messageRepository.countAllByChatRoomIdAndFromAndIsRead(chat.getId(), sender, false));
            }
        }
        return userChats;
    }

    public List<MessageDto> findChatMessages(UUID chatId) {
        List<Message> messages = messageRepository.findAllByChatRoomIdOrderBySentAtAsc(chatId);
        List<MessageDto> messageDtoList = new ArrayList<>();
        for (Message message : messages) {
            MessageDto messageDto = new MessageDto();
            messageDto.setMessage(message.getMessage());
            messageDto.setChatRoom(message.getChatRoom());
            messageDto.setIsRead(message.getIsRead());
            messageDto.setFrom(message.getFrom());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formatDateTime = message.getSentAt().format(formatter);
            messageDto.setSentAt(formatDateTime);
            messageDto.setId(message.getId());
            messageDtoList.add(messageDto);
        }
        return messageDtoList;
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

    public List<User> searchUsersForHost(UUID hostId, String email) {
        if (hostId == null || !userRepository.existsById(hostId)) return null;

        User host = userRepository.findById(hostId).orElse(null);
        List<User> wantedUsers = userRepository.findAllByEmailContaining(email);
        wantedUsers.removeIf(user -> user.equals(host));
        return wantedUsers;
    }

    public void makeChatMessagesRead(UUID chatId, UUID userId) {
        if (chatRepository.existsById(chatId)) {
            List<Message> messages = messageRepository.findAllByChatRoomId(chatId);
            for (Message message : messages) {
                if (!message.getFrom().getId().equals(userId)) {
                    message.setIsRead(true);
                }
            }
        }
    }


    public void setLeftAtS(List<ChatRoom> chatRooms){
        for (ChatRoom chatRoom : chatRooms) {
            userService.setLeftAtS(chatRoom.getUser2());
            userService.setLeftAtS(chatRoom.getUser1());
        }
    }

    public User getGuestUser(UUID chatId, HttpServletRequest req, boolean isGuest) {
        UUID userId = (UUID) req.getSession().getAttribute("userId");
        ChatRoom chatRoom = chatRepository.getById(chatId);
        if (isGuest) {
            if (chatRoom.getUser2().getId().equals(userId)) {
                return chatRoom.getUser1();
            } else {
                return chatRoom.getUser2();
            }
        } else {
            if (chatRoom.getUser2().getId().equals(userId)) {
                return chatRoom.getUser2();
            } else {
                return chatRoom.getUser1();
            }
        }

    }

    public UUID saveMessage(UUID guestId, HttpServletRequest request, String message) {

            UUID userId = (UUID) request.getSession().getAttribute("userId");
            if (userId != null) {
                User guestUser = userRepository.getById(guestId);
                User user = userRepository.getById(userId);
                if (!chatRepository.existsByUser1IdAndUser2Id(guestId, userId) && !chatRepository.existsByUser1IdAndUser2Id(userId, guestId)) {
                    createChat(userId, guestId);
                }
                ChatRoom chatRoom1 = chatRepository.findByUser1AndUser2(guestUser, user);
                if(chatRoom1 == null){
                    chatRoom1 = chatRepository.findByUser1AndUser2(user, guestUser);
                }
                if (message.isBlank() || message.isEmpty()) {
                    return chatRoom1.getId();
                }
                Message message1 = new Message();
                message1.setMessage(message);
                message1.setChatRoom(chatRoom1);
                message1.setFrom(user);
                message1.setIsRead(false);
                message1.setSentAt(LocalDateTime.now());
                messageRepository.save(message1);
                return chatRoom1.getId();
            }
            return null;
    }

    public List<MessageDto> getNewMessage(List<MessageDto> chatMessages,UUID userId) {
        List<MessageDto> messageDtoList = new ArrayList<>();
        for (MessageDto chatMessage : chatMessages) {
            if (!chatMessage.getIsRead() && !chatMessage.getFrom().getId().equals(userId)) {
                messageDtoList.add(chatMessage);
            }
        }
        return messageDtoList;
    }

    public ChatRoom getEdit(UUID messageId, RedirectAttributes rA, HttpServletRequest rq) {
        Message message = messageRepository.findById(messageId).get();
        UUID userId = (UUID)rq.getSession().getAttribute("userId");
        ChatRoom chatRoom = chatRepository.getById(message.getChatRoom().getId());
        if(userId.equals(message.getFrom().getId())){
            rA.addFlashAttribute("editMessage", message);
            return chatRoom;
        }
        return chatRoom;
    }

    public UUID updateMessage(String message, UUID guestId, HttpServletRequest req, UUID eMI) {
        Message message1 = messageRepository.findById(eMI).get();
        message1.setMessage(message);
        messageRepository.save(message1);
        return message1.getChatRoom().getId();
    }


    public UUID deleteMessage(UUID msgId) {
        Message message = messageRepository.findById(msgId).get();
        messageRepository.delete(message);
        return message.getChatRoom().getId();
    }

    public void setLeftAtS(List<User> wantedUsers, boolean noSame) {
        for (User user : wantedUsers) {
            userService.setLeftAtS(user);
        }
    }
}
