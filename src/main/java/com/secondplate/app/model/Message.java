package com.secondplate.app.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    private Long eventId;      // FK -> Event.eventId
    private Long senderUserId; // FK -> User.userId
    private Long recipientUserId; // FK -> User.userId
    private String body;
    private LocalDateTime sentAt;

    public Long getMessageId() { return messageId; }
    public void setMessageId(Long messageId) { this.messageId = messageId; }

    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }

    public Long getSenderUserId() { return senderUserId; }
    public void setSenderUserId(Long senderUserId) { this.senderUserId = senderUserId; }

    public Long getRecipientUserId() { return recipientUserId; }
    public void setRecipientUserId(Long recipientUserId) { this.recipientUserId = recipientUserId; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
