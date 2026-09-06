package com.secondplate.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.secondplate.app.model.Event;
import com.secondplate.app.model.User;
import com.secondplate.app.service.EventService;
import com.secondplate.app.service.UserService;

@Controller
public class PageController {

    private final EventService eventService;
    private final UserService userService;

    public PageController(EventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String usernameOrEmail, @RequestParam String password) {
        // Placeholder only: no credential check happens yet. Spring Security and
        // password hashing aren't set up in this project, so anyone submitting the
        // form is redirected home without being authenticated.
        return "redirect:/";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register-page";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String fullName,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String passwordConfirmation,
            @RequestParam String phoneNumber,
            Model model) {
        if (!password.equals(passwordConfirmation)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("fullName", fullName);
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("phoneNumber", phoneNumber);
            return "register-page";
        }
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        userService.createUser(user);
        return "redirect:/login?registered";
    }

    @GetMapping("/host-event")
    public String hostEvent() {
        return "host-event";
    }

    @GetMapping("/host-event/details")
    public String eventDetails(@RequestParam(defaultValue = "one-time") String type, Model model) {
        boolean recurring = "recurring".equalsIgnoreCase(type);
        model.addAttribute("recurring", recurring);
        model.addAttribute("eventType", recurring ? "Recurring Event" : "One-Time Event");
        return "host-event-details";
    }

    @PostMapping("/host-event/details")
    public String createEvent(
            @RequestParam String title,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalTime time,
            @RequestParam String location,
            @RequestParam Integer capacity,
            @RequestParam(required = false) String frequency,
            @RequestParam(required = false) java.math.BigDecimal ticketPrice,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(defaultValue = "one-time") String type) {
        boolean recurring = "recurring".equalsIgnoreCase(type);
        Event event = new Event();
        event.setTitle(title);
        LocalDate eventDate = recurring ? startDate : date;
        LocalTime eventTime = time == null ? LocalTime.MIDNIGHT : time;
        event.setEventTime(eventTime);
        event.setLocation(location);
        event.setCapacity(capacity);
        event.setRecurring(recurring);
        event.setFrequency(recurring ? frequency : null);
        event.setTicketPrice(ticketPrice);
        event.setDescription(description);
        eventService.createEvent(event);
        return "redirect:/host-event?created";
    }
}
