package com.tamizhselvan.pingme.chatroom;

import com.tamizhselvan.pingme.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySender_UserNameAndRecipient_UserName(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        // Create ChatRoom entities
        ChatRoom senderRecipient = ChatRoom
                .builder()
                .id(UUID.randomUUID()) // Generate a new UUID
                .chatId(chatId)
                .sender(Users.builder().userName(senderId).build()) // Assuming `Users` is correctly set
                .recipient(Users.builder().userName(recipientId).build()) // Assuming `Users` is correctly set
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .id(UUID.randomUUID()) // Generate a new UUID
                .chatId(chatId)
                .sender(Users.builder().userName(recipientId).build()) // Assuming `Users` is correctly set
                .recipient(Users.builder().userName(senderId).build()) // Assuming `Users` is correctly set
                .build();

        // Save both entities
        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
