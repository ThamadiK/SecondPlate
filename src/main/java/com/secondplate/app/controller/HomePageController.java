package com.secondplate.app.controller;

import com.secondplate.app.model.Event;
import com.secondplate.app.service.BookingService;
import com.secondplate.app.service.EventService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Handles HTTP requests
@Controller
public class HomePageController {

    @Autowired
    private EventService eventService;

    // "/" has no page of its own - login is the front door, so anyone not
    // signed in lands there first; a signed-in visitor goes straight to the feed.
    @GetMapping("/")
    public String root(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }
        return "redirect:/home";
    }

    @Autowired
    private BookingService bookingService; 

    // maps the URL "/home" to this method
    @GetMapping("/home")
    public String showHomePage(
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String dietaryTags,
            @RequestParam(required = false) String timeOfDay,
            Model model) {

        // Model is a data structure that holds the data we want to pass to the view
        // The view is the HTML template that will be rendered and sent to the user's browser
        // Make events available to the view under the name "events" (the name bthe HTML template uses to access the data)        
        model.addAttribute("events", eventService.getFilteredEvents(cuisine, dietaryTags, timeOfDay));

        // echo the applied filters back so the template can show them as removable chips maybe later on?
        model.addAttribute("appliedCuisine", cuisine);
        model.addAttribute("appliedDietary", dietaryTags);
        model.addAttribute("appliedTimeOfDay", timeOfDay);

        // data for the Recurring Get-Together Groups section
        List<Event> recurringEvents = eventService.getRecurringEvents();
        Map<Long, Long> bookingCounts = new LinkedHashMap<>();
        for (Event event : recurringEvents) {
            bookingCounts.put(event.getEventId(), bookingService.getBookingCount(event.getEventId()));
        }
        model.addAttribute("recurringEvents", recurringEvents);
        model.addAttribute("bookingCounts", bookingCounts);

        return "home"; // resolves to src/main/resources/templates/homePage.html
    }

}