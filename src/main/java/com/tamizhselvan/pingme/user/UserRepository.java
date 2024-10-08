package com.tamizhselvan.pingme.user;

import org.apache.catalina.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends  JpaRepository<Users, String> {
    List<Users> findAllByStatus(Users.Status status);
    List<Users> findAll();
}
