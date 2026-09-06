package com.secondplate.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

// This java class represents an entity
@Entity
// Stores objects in the "events" table in the database
@Table(name = "events")
public class Event {

    // The primary key field
    @Id
    // The database generates valuse for this field automatically. Values auto-increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;
    private Long organizerId;
    private String location;
    private LocalDate eventDate;
    private LocalTime eventTime;
    private Integer capacity;
    private boolean recurring;
    private String frequency;
    private BigDecimal ticketPrice;
    private String description;
    private String cuisineType;
    private String dietaryTags;
    private String timeOfDay;
    private String imageUrl;

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }

    public LocalTime getEventTime() { return eventTime; }
    public void setEventTime(LocalTime eventTime) { this.eventTime = eventTime; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public boolean getRecurring() { return recurring; }
    public void setRecurring(boolean recurring) { this.recurring = recurring; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getDietaryTags() { return dietaryTags; }
    public void setDietaryTags(String dietaryTags) { this.dietaryTags = dietaryTags; }

    public String getTimeOfDay() { return timeOfDay; }
    public void setTimeOfDay(String timeOfDay) { this.timeOfDay = timeOfDay; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}