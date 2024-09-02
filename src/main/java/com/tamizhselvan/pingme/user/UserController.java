package com.tamizhselvan.pingme.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000/home")
@RequiredArgsConstructor
public class UserController{
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public Users addUser(@Payload Users user) {//,SimpMessageHeaderAccessor headerAccesser

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
//        logger.info("User disconnected successfully: {}", user);
        return user;
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<Users>> findConnectedUsers() {
        logger.info("Received request to find connected users");
        List<Users> connectedUsers = userService.findConnectedUsers();
//        logger.info("Connected users: {}", connectedUsers);
        return ResponseEntity.ok(connectedUsers);
    }

//    @GetMapping("/api/userinfo")
//    public String getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
//        logger.info("OAuth2 user info: {}", principal.getAttributes());
//        // Process user info to save it in the database
//        userService.processOAuthPostLogin(principal);
//
//        // Redirect to the React frontend
//        return "redirect:http://localhost:3000/home";
//    }

    @GetMapping("/api/userinfo")
    public ResponseEntity<Map<String, Object>> getUserInfoJson(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", false);
            response.put("message", "User is not authenticated");
            return ResponseEntity.status(401).body(response);
        }

        Map<String, Object> userInfo = new HashMap<>(principal.getAttributes());
        userInfo.put("authenticated", true);

        logger.info("OAuth2 user info: {}", userInfo);
        return ResponseEntity.ok(userInfo);
    }

//    @GetMapping("/api/userinfo")
//    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
//        logger.info("OAuth2 user info: {}", principal.getAttributes());
//        userService.processOAuthPostLogin(principal);
//        return ResponseEntity.ok(principal.getAttributes());  // Return user info as JSON
//    }

    @GetMapping("/redirect-to-home")
    public String redirectToHome() {
        return "redirect:http://localhost:3000/home";  // Separate redirect logic
    }


//    @GetMapping("/api/userinfo")
//    public ResponseEntity<Map<String, String>> getUserInfo(@AuthenticationPrincipal OAuth2User principal) {
//        logger.info("OAuth2 user info: {}", principal.getAttributes());
//        userService.processOAuthPostLogin(principal);
//
//        Map<String, String> response = Map.of("redirectUrl", "http://localhost:3000/home");
//        return ResponseEntity.ok(response);
//    }


}
