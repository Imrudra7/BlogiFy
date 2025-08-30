package com.stranger.blogify.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
public class GlobalMethods {
    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean checkPassword(String rawPassword, String encodedPassword) {

        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    public String createIdByTimeAndCategory(String category) {
        UUID newId = UUID.randomUUID();
        String idString = newId.toString();
        String id = category+idString;
        return id;
    }
}
