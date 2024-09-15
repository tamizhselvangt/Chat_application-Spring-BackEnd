package com.tamizhselvan.pingme.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;


@Controller
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageService chatMessageService;

    @Autowired
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                new ChatNotification(
                        savedMsg.getChatId(),
                        savedMsg.getSenderId(),
                        savedMsg.getRecipientId(),
                        savedMsg.getContent()
                )
        );
    }

    // New API endpoint for file uploads
    @PostMapping("/upload/media")
    public ResponseEntity<ChatMessage> uploadMedia (@RequestParam("file") MultipartFile file,
                                               @RequestParam("senderId") String senderId,
                                               @RequestParam("recipientId") String recipientId) {
        try {


            ChatMessage mediaMessage = new ChatMessage();
            mediaMessage.setSenderId(senderId);
            mediaMessage.setRecipientId(recipientId);
            mediaMessage.setTimestamp(new Date());
            mediaMessage.setContent(file.getOriginalFilename());
            mediaMessage.setMediaData(file.getBytes()); // Store file as byte array if needed
            mediaMessage.setMediaType(file.getContentType()); // Store media type


            // Save the chat message to the database
            chatMessageService.save(mediaMessage);

            return ResponseEntity.ok(mediaMessage);
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
            return null;
        }
    }



    @GetMapping("/messages/media/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getUploadMedia(
            @PathVariable String senderId,
            @PathVariable String recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.findMediaChatMessages(senderId, recipientId));
    }


    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}




// Utility method to save file and ensure directory exists
//    private String saveFile(MultipartFile file) throws IOException {
//        // Ensure the upload directory exists
//        File uploadDir = new File(UPLOAD_DIR);
//        if (!uploadDir.exists()) {
//            boolean created = uploadDir.mkdirs(); // Create directories if they don't exist
//            if (!created) {
//                throw new IOException("Failed to create directory: " + UPLOAD_DIR);
//            }
//        }
//
//        // Generate a unique file name to avoid conflicts
//        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//        File targetFile = new File(UPLOAD_DIR + fileName);
//
//        // Save the file to the target directory
//        try (FileOutputStream fos = new FileOutputStream(targetFile)) {
//            fos.write(file.getBytes());
//        }
//
//        // Return the absolute path to the saved file
//        return targetFile.getAbsolutePath();
//    }