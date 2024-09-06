package com.tamizhselvan.pingme.chat;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(nullable = false)
    private String chatId;

    @Column(name = "sender_id", nullable = false)
    private String senderId;

    @Column(name = "recipient_id", nullable = false)
    private String recipientId;

    private String content;

    @Column(name = "time_stamp", nullable = false)
    private Date timestamp;

    // Store the file content directly in the database as byte[]
    @Lob
    @Column(name = "media_data")
    private byte[] mediaData;

    @Column(name = "media_type")
    private String mediaType;
}

