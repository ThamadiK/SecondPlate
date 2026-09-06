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
import com.secondplate.app.service.EventService;

@Controller
public class PageController {

    private final EventService eventService;

    public PageController(EventService eventService) {
        this.eventService = eventService;
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
            @RequestParam LocalDate date,
            @RequestParam LocalTime time,
            @RequestParam String location,
            @RequestParam Integer capacity,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(defaultValue = "one-time") String type) {
        Event event = new Event();
        event.setTitle(title);
        event.setEventTime(LocalDateTime.of(date, time));
        event.setLocation(location);
        event.setCapacity(capacity);
        event.setRecurring("recurring".equalsIgnoreCase(type));
        event.setDescription(description);
        eventService.createEvent(event);
        return "redirect:/host-event?created";
    }
}
