package com.stranger.blogify.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
OAuth2AuthenticationSuccessHandler auth2AuthenticationSuccessHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // disable CSRF for APIs
                .cors(cors -> {})              // ✅ CORS ko enable kiya (CorsConfig ke bean se pick karega)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/index.html").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(auth2AuthenticationSuccessHandler) // OAuth2 success redirect
                );

        return http.build();
    }
}
