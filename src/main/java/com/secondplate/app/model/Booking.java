package com.secondplate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {

    @Id // Marks the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // The primary key will be generated automatically, 
    private Long bookingId;
    private Long userId;    // FK -> User.userId (plain field for now, no relationship)
    private Long eventId;   // FK -> Event.eventId (plain field for now, no relationship)
}
