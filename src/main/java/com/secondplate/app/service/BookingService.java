package com.secondplate.app.service;

import com.secondplate.app.model.Booking;
import com.secondplate.app.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public Booking createBooking(Booking booking) {
        // business rules (e.g. "check event capacity before allowing booking") go here later
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsForEvent(Long eventId) {
        return bookingRepository.findByEventIdOrderByBookingIdAsc(eventId);
    }

    public boolean hasUserJoined(Long eventId, Long userId) {
        return bookingRepository.existsByEventIdAndUserId(eventId, userId);
    }

    public Booking joinEvent(Long eventId, Long userId) {
        if (hasUserJoined(eventId, userId)) {
            return null;
        }
        Booking booking = new Booking();
        booking.setEventId(eventId);
        booking.setUserId(userId);
        return bookingRepository.save(booking);
    }

    public long getBookingCount(Long eventId) {
        return bookingRepository.countByEventId(eventId);
    }
    
}
