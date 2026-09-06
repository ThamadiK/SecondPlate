package com.secondplate.app.service;

import com.secondplate.app.model.Message;
import com.secondplate.app.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getMessagesForEvent(Long eventId) {
        return messageRepository.findByEventIdOrderBySentAtAsc(eventId);
    }

    public Message postMessage(Long eventId, Long senderUserId, String body) {
        Message message = new Message();
        message.setEventId(eventId);
        message.setSenderUserId(senderUserId);
        message.setBody(body);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getConversation(Long firstUserId, Long secondUserId) {
        return messageRepository
                .findBySenderUserIdAndRecipientUserIdOrSenderUserIdAndRecipientUserIdOrderBySentAtAsc(
                        firstUserId, secondUserId, secondUserId, firstUserId);
    }

    public Message sendDirectMessage(Long senderUserId, Long recipientUserId, String body) {
        Message message = new Message();
        message.setSenderUserId(senderUserId);
        message.setRecipientUserId(recipientUserId);
        message.setBody(body);
        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }
}
