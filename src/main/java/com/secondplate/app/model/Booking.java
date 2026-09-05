package com.secondplate.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingId;
    private Long userId;    // FK -> User.userId (plain field for now, no relationship)
    private LocalDate bookingDate;
    private Long eventId;   // FK -> Event.eventId (plain field for now, no relationship)
}
