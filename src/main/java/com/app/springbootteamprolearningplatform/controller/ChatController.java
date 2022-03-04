package com.app.springbootteamprolearningplatform.controller;

import com.app.springbootteamprolearningplatform.model.ChatRoom;
import com.app.springbootteamprolearningplatform.model.Message;
import com.app.springbootteamprolearningplatform.model.User;
import com.app.springbootteamprolearningplatform.repository.RoleRepository;
import com.app.springbootteamprolearningplatform.service.ChatService;
import com.app.springbootteamprolearningplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/chats")
public class ChatController {
    private final ChatService chatService;
    private RoleRepository roleRepository;

    @Autowired
    public ChatController(ChatService chatService, RoleRepository roleRepository) {
        this.chatService = chatService;
        this.roleRepository = roleRepository;
    }

    //      GET USER CHATS
    @GetMapping
    public String getUserChats(Model model, HttpServletRequest request) {
        UUID userId = (UUID)request.getSession().getAttribute("userId");
        if(userId == null){
            return "login-form";
        }
        List<ChatRoom> userChats = chatService.getUserChats(userId);
        model.addAttribute("userChats", userChats);
        model.addAttribute("v-userId", userId);
        return "chat"; // todo create char room dto
    }

    //      SEND MESSAGE
    @PostMapping("/sendMessage/{chatId}")
    public String sendMessage(@PathVariable UUID chatId,
                              @RequestParam(name = "message") String message,
                              HttpServletRequest request) {
        boolean isSent = chatService.sendMessage(request, chatId, message);

        if (isSent) {
            return "redirect:/chats/messages/" + chatId;
        } else {
            return "redirect:/chats/" + UUID.fromString(request.getSession().getAttribute("userId").toString());
        }
    }

    //      GET CHAT MESSAGES
    @GetMapping("/messages/{chatId}")
    public String getChatMessages(@PathVariable UUID chatId, HttpServletRequest request, Model model) {

        //      SPECIFIC CHAT MESSAGES
        List<Message> chatMessages = chatService.findChatMessages(chatId);
        model.addAttribute("chatMessages", chatMessages);

        //      SESSION USER CHATS
        UUID userId = UUID.fromString(request.getSession().getAttribute("userId").toString());
        List<ChatRoom> userChats = chatService.getUserChats(userId);
        model.addAttribute("userChats", userChats);
        model.addAttribute("v-userId", userId);

        return "chat"; // todo create chat message dto
    }

    // CREATE CHAT WITH ANOTHER USER
    @PostMapping("/createChat/{guestId}")
    public String createChatWith(@PathVariable UUID guestId, HttpServletRequest request) {
        UUID hostId = UUID.fromString(request.getSession().getAttribute("userId").toString());

        //      HOST CREATES CHAT WITH GUEST
        boolean isChatCreated = chatService.createChat(hostId, guestId);

        return "redirect:/chats/" + hostId;
    }

    //      SEARCH USER BY EMAIL
    @GetMapping("/search")
    public String searchUser(@RequestParam(name = "email") String email, HttpServletRequest request, Model model) {
        UUID hostId = UUID.fromString(request.getSession().getAttribute("userId").toString());
        User wanted = chatService.searchUserForHost(hostId, email);
        model.addAttribute("wanted", wanted);
        return "view-wanted-user"; // TODO
    }
}
