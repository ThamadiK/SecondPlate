package com.secondplate.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.secondplate.app.model.Booking;
import com.secondplate.app.model.Event;
import com.secondplate.app.model.Message;
import com.secondplate.app.model.User;
import com.secondplate.app.service.BookingService;
import com.secondplate.app.service.EventService;
import com.secondplate.app.service.MessageService;
import com.secondplate.app.service.UserService;

@Controller
public class PageController {

    private static final String SESSION_USER_ID = "userId";

    private final EventService eventService;
    private final BookingService bookingService;
    private final UserService userService;
    private final MessageService messageService;

    public PageController(EventService eventService, BookingService bookingService,
                           UserService userService, MessageService messageService) {
        this.eventService = eventService;
        this.bookingService = bookingService;
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/login")
    public String login() {
        return "login-page";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String usernameOrEmail, @RequestParam String password,
                               HttpSession session, Model model) {
        User user = userService.findByUsernameOrEmail(usernameOrEmail);
        if (user == null || user.getPassword() == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "Invalid username/email or password");
            model.addAttribute("usernameOrEmail", usernameOrEmail);
            return "login-page";
        }
        session.setAttribute(SESSION_USER_ID, user.getUserId());
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
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
    public String hostEvent(HttpSession session) {
        if (currentUserId(session) == null) {
            return "redirect:/login";
        }
        return "host-event";
    }

    @GetMapping("/host-event/details")
    public String eventDetails(@RequestParam(defaultValue = "one-time") String type, HttpSession session, Model model) {
        if (currentUserId(session) == null) {
            return "redirect:/login";
        }
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
            @RequestParam(defaultValue = "one-time") String type,
            HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        boolean recurring = "recurring".equalsIgnoreCase(type);
        Event event = new Event();
        event.setTitle(title);
        LocalDate eventDate = recurring ? startDate : date;
        LocalTime eventTime = time == null ? LocalTime.MIDNIGHT : time;
        event.setEventDate(eventDate);
        event.setEventTime(eventTime);
        event.setLocation(location);
        event.setCapacity(capacity);
        event.setRecurring(recurring);
        event.setFrequency(recurring ? frequency : null);
        event.setTicketPrice(ticketPrice);
        event.setDescription(description);
        event.setOrganizerId(userId);
        eventService.createEvent(event);
        return "redirect:/host-event?created";
    }

    @GetMapping("/events/{id}")
    public String viewEvent(@PathVariable Long id, HttpSession session, Model model) {
        Event event = requireEvent(id);
        Long userId = currentUserId(session);
        model.addAttribute("event", event);
        model.addAttribute("organizerName", resolveOrganizerName(event));
        model.addAttribute("currentUserId", userId);

        if (event.getRecurring()) {
            List<Booking> bookings = bookingService.getBookingsForEvent(id);
            List<User> attendees = bookings.stream()
                    .map(b -> userService.getUserById(b.getUserId()))
                    .filter(u -> u != null)
                    .collect(Collectors.toList());
            model.addAttribute("attendees", attendees);
            model.addAttribute("alreadyJoined", userId != null && bookingService.hasUserJoined(id, userId));

            List<Message> messages = messageService.getMessagesForEvent(id);
            Map<Long, User> usersById = attendees.stream()
                    .collect(Collectors.toMap(User::getUserId, Function.identity(), (a, b) -> a));
            model.addAttribute("messages", messages);
            model.addAttribute("usersById", usersById);
        } else {
            model.addAttribute("tags", buildTags(event));
        }
        return "event-detail";
    }

    @GetMapping("/events/{id}/book")
    public String bookEventForm(@PathVariable Long id, HttpSession session, Model model) {
        if (currentUserId(session) == null) {
            return "redirect:/login";
        }
        Event event = requireEvent(id);
        if (event.getRecurring()) {
            return "redirect:/events/" + id;
        }
        model.addAttribute("event", event);
        model.addAttribute("organizerName", resolveOrganizerName(event));
        return "event-booking";
    }

    @PostMapping("/events/{id}/book")
    public String bookEvent(
            @PathVariable Long id,
            @RequestParam String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String cardNumber,
            @RequestParam(required = false) String cardExpiry,
            @RequestParam(required = false) String dietaryRequirements,
            HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        requireEvent(id);
        Booking booking = new Booking();
        booking.setEventId(id);
        booking.setUserId(userId);
        booking.setEmail(email);
        booking.setPhoneNumber(phoneNumber);
        booking.setFirstName(firstName);
        booking.setLastName(lastName);
        booking.setCardNumber(cardNumber);
        booking.setCardExpiry(cardExpiry);
        booking.setDietaryRequirements(dietaryRequirements);
        bookingService.createBooking(booking);
        return "redirect:/events/" + id + "?booked";
    }

    @PostMapping("/events/{id}/join")
    public String joinEvent(@PathVariable Long id, HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        requireEvent(id);
        bookingService.joinEvent(id, userId);
        return "redirect:/events/" + id;
    }

    @PostMapping("/events/{id}/messages")
    public String postMessage(@PathVariable Long id, @RequestParam String body, HttpSession session) {
        Long userId = currentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        requireEvent(id);
        if (body != null && !body.isBlank()) {
            messageService.postMessage(id, userId, body.trim());
        }
        return "redirect:/events/" + id;
    }

    private Long currentUserId(HttpSession session) {
        return (Long) session.getAttribute(SESSION_USER_ID);
    }

    private Event requireEvent(Long id) {
        Event event = eventService.getEventById(id);
        if (event == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found");
        }
        return event;
    }

    private String resolveOrganizerName(Event event) {
        if (event.getOrganizerId() == null) {
            return "Unknown host";
        }
        User organizer = userService.getUserById(event.getOrganizerId());
        return organizer == null ? "Unknown host" : organizer.getFullName();
    }

    private List<String> buildTags(Event event) {
        List<String> tags = new ArrayList<>();
        if (event.getCuisineType() != null && !event.getCuisineType().isBlank()) {
            tags.add(event.getCuisineType().trim());
        }
        if (event.getDietaryTags() != null && !event.getDietaryTags().isBlank()) {
            for (String tag : event.getDietaryTags().split(",")) {
                if (!tag.isBlank()) {
                    tags.add(tag.trim());
                }
            }
        }
        return tags;
    }
}
