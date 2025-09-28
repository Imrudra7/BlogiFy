package com.stranger.blogify.service;

import com.stranger.blogify.model.AuthProvider; // <-- Import the enum
import com.stranger.blogify.model.User;
import com.stranger.blogify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

import java.time.ZoneId;
import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // The find-or-create logic is great!
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setRole("USER"); // Or whatever your default role is

                    // IMPROVEMENT 1: Use the AuthProvider enum
                    newUser.setAuthProvider(AuthProvider.GOOGLE);

                    // IMPROVEMENT 2: Use Instant.now() for timestamps
                    newUser.setCreatedAt(Instant.now());

                    newUser.setActive(true);
                    return newUser;
                });

        // This is fine for updating the last login time
        user.setLastLogin(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()));
        List<Date> logDays = user.getLoggedInDays();
        if(null == logDays) logDays = new ArrayList<>();
        logDays.add(new Date());
        user.setLoggedInDays(logDays);

        userRepository.save(user);

        // --- CRITICAL FIX ---
        // We must create a new principal that includes the roles from OUR database.

        // 1. Get the user's role from your User object
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole())); // Spring Security expects "ROLE_" prefix
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // Default role
        }

        // 2. Return a new DefaultOAuth2User with the correct authorities
        return new DefaultOAuth2User(
                authorities,
                oAuth2User.getAttributes(),
                "email" // The attribute to use as the principal name
        );
    }
}