package com.example.eventapp.service;

import com.example.eventapp.model.Booking;
import com.example.eventapp.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
