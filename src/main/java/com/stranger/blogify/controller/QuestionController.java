package com.stranger.blogify.controller;

import com.stranger.blogify.common.ApiResponse;
import com.stranger.blogify.config.JwtTokenProvider;
import com.stranger.blogify.model.Question;
import com.stranger.blogify.model.User;
import com.stranger.blogify.service.AccountService;
import com.stranger.blogify.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/dsa")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    AccountService accountService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @GetMapping("/loadAllQuestions")
    ResponseEntity<List<Question>> loadAllQuestions () {
        return new ResponseEntity<>( questionService.loadAllQuestions(), HttpStatusCode.valueOf(200));
    }
    @PostMapping("/addNewQuestion")
    ResponseEntity<ApiResponse> addQuestion (@RequestBody Question question) {
        if (question == null || question.getName() == null || question.getName().trim().isEmpty()
                || question.getSection() == null || question.getSection().trim().isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse("Question is empty or invalid."),
                    HttpStatus.BAD_REQUEST
            );
        }

        Question newQuestion = questionService.addQuestion(question);
        if (newQuestion != null) {
            return new ResponseEntity<>(
                    new ApiResponse("Question " + newQuestion.getName() + " created successfully."),
                    HttpStatus.CREATED
            );
        } else {
            return new ResponseEntity<>(
                    new ApiResponse("Something went wrong."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    @PostMapping ("/addListQuestions")
    ResponseEntity<ApiResponse> addQuestion (@RequestBody List<Question> questionList) {
        if(null == questionList || questionList.isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse("Question List is empty or invalid."),
                    HttpStatus.BAD_REQUEST
            );
        }
        List<Question> created= questionService.addQuestion(questionList);
        if(created!=null) {
            return new ResponseEntity<>(
                    new ApiResponse("Questions created successfully.", created, true),
                    HttpStatus.CREATED
            );
        }
         return new ResponseEntity<>(
                new ApiResponse("Something went wrong.",null,false),
                HttpStatus.BAD_REQUEST
        );
    }
    @PatchMapping("/updateSelection")
    ResponseEntity<ApiResponse> doneQuestion (@RequestBody User user) {

        if (user == null || user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return new ResponseEntity<>(
                    new ApiResponse("User data is empty or invalid."),
                    HttpStatus.BAD_REQUEST
            );
        }

        User newUser = accountService.questionDone(user);
        if (newUser != null) {
            return new ResponseEntity<>(
                    newUser.getAddOrRemove() != null && newUser.getAddOrRemove().equals("Add")
                            ? new ApiResponse("Well done!! 💥💥 " + newUser.getName() + " done successfully.✅", newUser, true)
                            : new ApiResponse("Next time achhe se try karna bhai..🥲!!", newUser, true),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(
                    new ApiResponse("Something went wrong."),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    @GetMapping("/getUserSelections")
    public ResponseEntity<ApiResponse> getUserSelections(
            @RequestHeader("Authorization") String authHeader) {

        // authHeader => "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Token missing or invalid"));
        }

        String token = authHeader.substring(7); // "Bearer " remove

        String email = jwtTokenProvider.extractUsername(token);

        // Ab email se database fetch kar sakte ho
        List<String> selections =  accountService.getUserSelections(email);

        return ResponseEntity.ok(new ApiResponse("Success", selections, true));
    }

}
