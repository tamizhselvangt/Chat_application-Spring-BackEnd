package com.tamizhselvan.pingme.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
    Optional<ChatRoom> findBySender_NickNameAndRecipient_NickName(String senderId, String recipientId);
}
