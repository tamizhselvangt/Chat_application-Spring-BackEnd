package com.tamizhselvan.pingme.chatroom;

import com.tamizhselvan.pingme.user.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    private UUID id; // Use UUID for uniqueness

    private String chatId;

    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "user_name")
    private Users sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "user_name")
    private Users recipient;
}
