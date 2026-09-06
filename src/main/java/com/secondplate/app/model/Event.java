package com.secondplate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

// This java class represents an entity
@Entity
// Stores objects in the "events" table in the database
@Table(name = "events")

// Lombok automatically generates getters and setters for all fields
@Getter
@Setter
public class Event {

    // The primary key field
    @Id
    // The database generates valuse for this field automatically. Values auto-increment
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private String title;
    private Long organizerId;   
    private String location;
    private LocalDateTime eventTime;
    private Integer capacity;
    private boolean recurring;
    private String frequency;
    private BigDecimal ticketPrice;
    private String description;
    private String cuisineType; 
    private String dietaryTags;  
    private String timeOfDay;   
    private String imageUrl;     
}