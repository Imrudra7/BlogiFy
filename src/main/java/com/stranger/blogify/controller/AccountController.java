package com.stranger.blogify.controller;

import com.stranger.blogify.common.ApiResponse;
import com.stranger.blogify.config.JwtTokenProvider;
import com.stranger.blogify.model.User;
import com.stranger.blogify.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private final AccountService accountService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/registerUser")
    ResponseEntity<ApiResponse> createNewAccount (@RequestBody User user) {

        boolean registered = accountService.createNewAccount(user);
        if (registered) {
            return new ResponseEntity<>(new ApiResponse("User registered successfully."), HttpStatusCode.valueOf(201));
        }
        return new ResponseEntity<>(new ApiResponse("User already registered with this email."), HttpStatusCode.valueOf(409));
    }

    @PostMapping("/login")
    ResponseEntity<ApiResponse> login (@RequestBody User user) {
        User loginUser = accountService.login(user);
        if(loginUser == null) {
            return new ResponseEntity<>(new ApiResponse("Wrong email or password."), HttpStatusCode.valueOf(404));
        }
        String token = tokenProvider.generateToken(loginUser.getEmail());
        return new ResponseEntity<> (new ApiResponse("Welcome "+loginUser.getName()+"!!",token,true),HttpStatusCode.valueOf(200));
    }

}
