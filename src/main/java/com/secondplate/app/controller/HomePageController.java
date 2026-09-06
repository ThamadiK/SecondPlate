package com.secondplate.app.controller;

import com.secondplate.app.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Handles HTTP requests
@Controller 
public class HomePageController {

    @Autowired
    private EventService eventService;

    // maps the URL "/homePage" to this method
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

        return "home"; // resolves to src/main/resources/templates/homePage.html
    }
}