package com.secondplate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "events")
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    private Long organizerId;   
    private String location;
    private LocalDateTime evenTime;
    private Integer capacity;
    private String description;
}
