package com.tamizhselvan.pingme.chat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Controller
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage (@Payload ChatMessage chatMessage) {
        try {
            chatMessage.setTimestamp(new Date());
            logger.info("Received a Chat Message: {}", chatMessage);

            // Save regular chat messages
            ChatMessage savedMsg = chatMessageService.save(chatMessage);

            messagingTemplate.convertAndSendToUser(
                    chatMessage.getRecipientId(),
                    "/user/" + chatMessage.getRecipientId() + "/queue/messages",
                    new ChatNotification(
                            savedMsg.getChatId(),
                            savedMsg.getSenderId(),
                            savedMsg.getRecipientId(),
                            savedMsg.getContent()
                    )
            );
        } catch (Exception ex) {
            logger.error("Error processing message: {}", ex.getMessage());
        }
    }

    // New API endpoint for file uploads
    @PostMapping("/upload/media")
    public ResponseEntity<String> uploadMedia (@RequestParam("file") MultipartFile file,
                                               @RequestParam("senderId") String senderId,
                                               @RequestParam("recipientId") String recipientId) {
        try {


            ChatMessage mediaMessage = new ChatMessage();
            mediaMessage.setSenderId(senderId);
            mediaMessage.setRecipientId(recipientId);
            mediaMessage.setTimestamp(new Date());
            mediaMessage.setContent("Media file uploaded: " + file.getOriginalFilename());
            mediaMessage.setMediaData(file.getBytes()); // Store file as byte array if needed
            mediaMessage.setMediaType(file.getContentType()); // Store media type


            // Save the chat message to the database
            chatMessageService.save(mediaMessage);

            return ResponseEntity.ok("File uploaded successfully: " + file.getOriginalFilename());
        } catch (IOException e) {
            logger.error("Error saving file: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file.");
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

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String senderId,
                                                              @PathVariable String recipientId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
}