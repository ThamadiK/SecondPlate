package com.secondplate.app.service;

import com.secondplate.app.model.Event;
import com.secondplate.app.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    // Find an appropriate object for this dependency and give it to me.
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        // Returns a list of all events in the database. The findAll() method is provided by Spring Data JPA.
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public Event createEvent(Event event) {
        // Business rules (e.g. "capacity must be > 0") go here later
        return eventRepository.save(event);
    }

    public List<Event> getFilteredEvents(String cuisine, String dietaryTags, String timeOfDay) {
        // filter -> Keep only the elements that satisfy this condition. 
        // e represents each individual event
        return eventRepository.findAll().stream()
                .filter(e -> isBlank(cuisine) || cuisine.equalsIgnoreCase(e.getCuisineType()))
                .filter(e -> isBlank(dietaryTags) || (e.getDietaryTags() != null
                        && e.getDietaryTags().toLowerCase().contains(dietaryTags.toLowerCase())))
                .filter(e -> isBlank(timeOfDay) || timeOfDay.equalsIgnoreCase(e.getTimeOfDay()))
                .toList();
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public List<Event> getRecurringEvents() {
        return eventRepository.findByRecurring(true);
    }
}