package com.tamizhselvan.pingme.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public Users addUser(@Payload Users user) {//,SimpMessageHeaderAccessor headerAccesser

        System.out.println(user);
//        logger.info("Received request to add user: {}", user);
//        userService.saveUser(user);
//        logger.info("User added successfully: {}", user);
//        return user;
        try {
            Users savedUser = userService.saveUser(user);
            logger.info("User added successfully: {}", savedUser);
            return savedUser;
        } catch (Exception e) {
            logger.error("Error adding user: {}", e.getMessage(), e);
            return null;
        }
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public Users disconnectUser(
            @Payload Users user
    ) {
        logger.info("Received request to disconnect user: {}", user);
        userService.disconnect(user);
        logger.info("User disconnected successfully: {}", user);
        return user;
    }

    @GetMapping("/users")
    public ResponseEntity<List<Users>> findConnectedUsers() {
        logger.info("Received request to find connected users");
        List<Users> connectedUsers = userService.findConnectedUsers();
        logger.info("Connected users: {}", connectedUsers);
        return ResponseEntity.ok(connectedUsers);
    }
}
