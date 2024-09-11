package com.tamizhselvan.pingme.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findByChatId(String chatId);


    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatId = :chatId AND cm.mediaType IS NOT NULL AND cm.mediaData IS NOT NULL")
    List<ChatMessage> findMediaMessagesByChatId(String chatId);
}
