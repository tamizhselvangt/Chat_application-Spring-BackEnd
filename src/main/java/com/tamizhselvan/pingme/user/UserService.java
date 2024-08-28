package com.tamizhselvan.pingme.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(Users user) {
        user.setStatus(Users.Status.ONLINE);
        repository.save(user);
    }

    @Transactional
    public void disconnect(Users user) {
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        if (storedUser != null) {
            storedUser.setStatus(Users.Status.OFFLINE);
            repository.save(storedUser);
        }
        // Consider handling the case where the user is not found (optional)
    }

    public List<Users> findConnectedUsers() {
        return repository.findAllByStatus(Users.Status.ONLINE);
    }
}
