package com.secondplate.app.service;

import com.secondplate.app.model.Event;
import com.secondplate.app.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Comparator;

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

    public List<String> getAvailableCuisineTypes() {
        return eventRepository.findAll().stream()
                .map(Event::getCuisineType)
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public List<String> getAvailableDietaryTags() {
        return eventRepository.findAll().stream()
                .map(Event::getDietaryTags)
                .filter(value -> value != null && !value.isBlank())
                .flatMap(value -> java.util.Arrays.stream(value.split(",")))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .distinct()
                .sorted(Comparator.naturalOrder())
                .toList();
    }

    public List<Event> getEventsHostedBy(Long organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    public Event createEvent(Event event) {
        // Business rules (e.g. "capacity must be > 0") go here later
        return eventRepository.save(event);
    }

    public List<Event> getFilteredEvents(String search, String cuisine, String dietaryTags, String timeOfDay) {
        // filter -> Keep only the elements that satisfy this condition. 
        // e represents each individual event
        return eventRepository.findAll().stream()
                        .filter(e -> isBlank(search) || matchesSearch(e, search))
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

    private boolean matchesSearch(Event e, String search) {
        String term = search.toLowerCase();
        return containsIgnoreCase(e.getTitle(), term)
            || containsIgnoreCase(e.getLocation(), term)
            || containsIgnoreCase(e.getDescription(), term)
            || containsIgnoreCase(e.getCuisineType(), term);
    }
        
    private boolean containsIgnoreCase(String field, String term) {
        return field != null && field.toLowerCase().contains(term);
    }
}