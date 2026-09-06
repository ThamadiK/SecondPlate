package com.secondplate.app.controller;

import com.secondplate.app.model.User;
import com.secondplate.app.service.EventService;
import com.secondplate.app.service.MessageService;
import com.secondplate.app.service.ProfileImageService;
import com.secondplate.app.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class SocialController {

    private static final String SESSION_USER_ID = "userId";

    private final EventService eventService;
    private final MessageService messageService;
    private final ProfileImageService profileImageService;
    private final UserService userService;

    public SocialController(EventService eventService, MessageService messageService,
                            ProfileImageService profileImageService, UserService userService) {
        this.eventService = eventService;
        this.messageService = messageService;
        this.profileImageService = profileImageService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("hostedEvents", eventService.getEventsHostedBy(userId));
        return "profile-page";
    }

    @PostMapping("/profile/image")
    public String uploadProfileImage(@RequestParam("image") MultipartFile image,
                                     HttpSession session) throws IOException {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        User user = userService.getUserById(userId);
        if (user != null) {
            profileImageService.saveProfileImage(user, image);
        }
        return "redirect:/profile";
    }

    @GetMapping("/friends")
    public String friends(HttpSession session, @RequestParam(required = false) Long friendId,
                          @RequestParam(required = false) String q, Model model) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }

        User currentUser = userService.getUserById(userId);
        List<User> friends = userService.getAllUsers().stream()
                .filter(user -> !user.getUserId().equals(userId))
                .toList();
        User selectedFriend = friends.stream()
                .filter(friend -> friendId != null && friend.getUserId().equals(friendId))
                .findFirst()
                .orElse(friends.isEmpty() ? null : friends.get(0));

        List<User> searchResults = userService.searchByUsername(q).stream()
                .filter(user -> !user.getUserId().equals(userId))
                .toList();

        model.addAttribute("user", currentUser);
        model.addAttribute("friends", friends);
        model.addAttribute("selectedFriend", selectedFriend);
        model.addAttribute("searchQuery", q);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("messages", selectedFriend == null
                ? List.of()
                : messageService.getConversation(userId, selectedFriend.getUserId()));
        return "friends-page";
    }

    @PostMapping("/friends/messages")
    public String sendDirectMessage(@RequestParam Long friendId, @RequestParam String content,
                                    HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        if (!content.isBlank() && userService.getUserById(friendId) != null) {
            messageService.sendDirectMessage(userId, friendId, content.trim());
        }
        return "redirect:/friends?friendId=" + friendId;
    }

    private Long currentUserId(HttpSession session) {
        return (Long) session.getAttribute(SESSION_USER_ID);
    }
}
