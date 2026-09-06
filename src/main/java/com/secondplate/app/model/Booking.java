package com.secondplate.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id // Marks the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // The primary key will be generated automatically, 
    private Long bookingId;
    private Long userId;    
    private Long eventId;   

    public Long getBookingId() { return bookingId; }
    public void setBookingID(Long bookingId) { this.bookingId = bookingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

}
