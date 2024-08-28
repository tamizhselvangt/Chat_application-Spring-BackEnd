package com.tamizhselvan.pingme.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Users, String> {
    List<Users> findAllByStatus(Users.Status status);
}
