package com.secondplate.app.repository;

import com.secondplate.app.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByEventIdOrderByBookingIdAsc(Long eventId);
    boolean existsByEventIdAndUserId(Long eventId, Long userId);
}
