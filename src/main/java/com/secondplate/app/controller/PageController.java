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
        event.setEventTime(LocalDateTime.of(eventDate, eventTime));
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
