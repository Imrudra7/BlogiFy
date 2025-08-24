package com.stranger.blogify.service;

import com.stranger.blogify.common.GlobalMethods;
import com.stranger.blogify.model.User;
import com.stranger.blogify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    public final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GlobalMethods globalMethods;

    public AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public boolean createNewAccount(User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());

        if(optional.isPresent()) {
            return false;
        }
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        User newUser = new User();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        String id = LocalDateTime.now().format(formatter);
        newUser.setId(id);
        newUser.setEmail(user.getEmail());
        newUser.setPassword(hashedPassword);
        newUser.setName(user.getName());
        newUser.setStatus(user.getStatus() !=null ? user.getStatus() : "Slept");
        newUser.setCreatedAt(Instant.now());
        newUser.setUpdatedAt(Instant.now());


        userRepository.save(newUser);
        return true;
    }

    public User login (User user) {
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (optional.isEmpty()) {
            return null;
        }
        User dbUser = optional.get();
        boolean isPasswordMatched = globalMethods.checkPassword(user.getPassword(), dbUser.getPassword());

        if (!isPasswordMatched) {
            return null;
        }
        return dbUser;
    }
}
