package com.secondplate.app.repository;

import com.secondplate.app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEventIdOrderBySentAtAsc(Long eventId);

    List<Message> findBySenderUserIdAndRecipientUserIdOrSenderUserIdAndRecipientUserIdOrderBySentAtAsc(
            Long senderUserId,
            Long recipientUserId,
            Long reverseSenderUserId,
            Long reverseRecipientUserId);
}
