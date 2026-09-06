package com.secondplate.app.repository;

import com.secondplate.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    // The query is derived automatically
    List<Event> findByRecurring(boolean recurring);
}
