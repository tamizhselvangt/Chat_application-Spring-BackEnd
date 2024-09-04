package com.tamizhselvan.pingme.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public Users saveUser(Users user) {
        user.setStatus(Users.Status.ONLINE);
        repository.save(user);
        return user;
    }

    @Transactional
    public void disconnect(Users user) {
        var storedUser = repository.findById(user.getUserName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Users.Status.OFFLINE);
            repository.save(storedUser);
        }
        // Consider handling the case where the user is not found (optional)
    }

    public List<Users> findConnectedUsers() {
//        return repository.findAllByStatus(Users.Status.ONLINE);
        return repository.findAll();
    }


    public void processOAuthPostLogin(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<Users> existingUser = repository.findById(email);

        if (existingUser.isEmpty()) {
            // Create a new user if one doesn't exist
            Users newUser = new Users();
            newUser.setUserName(name);  // Use name as nickName
            newUser.setEmail(email); // Use email as fullName
            newUser.setStatus(Users.Status.ONLINE);
            repository.save(newUser);
        }
    }
}
