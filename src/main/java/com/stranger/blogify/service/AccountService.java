package com.stranger.blogify.service;

import com.stranger.blogify.common.GlobalMethods;
import com.stranger.blogify.model.User;
import com.stranger.blogify.repository.QuestionRepository;
import com.stranger.blogify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    public final UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    GlobalMethods globalMethods;
    @Autowired
    private QuestionRepository questionRepository;

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
        List<Date> logDays = dbUser.getLoggedInDays();
        if (null == logDays) logDays = new ArrayList<>();
        logDays.add(new Date());
        dbUser.setLoggedInDays(logDays);
        userRepository.save(dbUser);
        return dbUser;
    }

    public User questionDone(User user) {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        if(optionalUser.isEmpty()) {
            return null;
        } else {
            User updatedUser = optionalUser.get();

            List<String> questionsDone = updatedUser.getQuestionsDone();
            if(questionsDone != null) {
                if (!questionsDone.contains(user.getQuestionToBeAddedOrRemoved()) && user.getAddOrRemove().equals( "Add")) {
                    questionsDone.add(user.getQuestionToBeAddedOrRemoved());
                } else if (questionsDone.contains(user.getQuestionToBeAddedOrRemoved()) && user.getAddOrRemove().equals( "Remove")) {
                    questionsDone.remove(user.getQuestionToBeAddedOrRemoved());
                }
            } else {
                questionsDone = new ArrayList<>();
                if ( user.getAddOrRemove().equals( "Add")) {
                    questionsDone.add(user.getQuestionToBeAddedOrRemoved());
                }
            }
            updatedUser.setQuestionsDone(questionsDone);

            updatedUser.setAddOrRemove(user.getAddOrRemove());
            userRepository.save(updatedUser);

            return updatedUser;
        }
    }

    public List<String> getUserSelections(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return optionalUser.map(User::getQuestionsDone).orElse(null);
    }

    public List<Date> getStreakDays(User user) {
        List<Date> streaks = new ArrayList<>();
        if (!user.getEmail().isEmpty()) {
            Optional <User> optional = userRepository.findByEmail(user.getEmail());
            if (optional.isPresent()){
                streaks = optional.get().getLoggedInDays();
                return streaks;
            }
        }
        return streaks;
    }
}
