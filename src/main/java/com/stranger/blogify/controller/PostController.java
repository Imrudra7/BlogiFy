package com.stranger.blogify.controller;

import com.stranger.blogify.common.ApiResponse;
import com.stranger.blogify.model.Post;
import com.stranger.blogify.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping("/createPost")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.createPost(post));
    }
    @GetMapping("/getPostById/{id}")
    public ResponseEntity<Optional<Post>> getPostById(@PathVariable("id") String id) {
        Optional<Post> postOptional = postService.getPostById(id);
        if(postOptional.isPresent()) {
            return ResponseEntity.ok(postOptional);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/deletePostById/{id}")
    public ResponseEntity<ApiResponse> deletePostById (@PathVariable ("id") String id) {
        postService.deletePostById(id);
        ApiResponse successResponse = new ApiResponse("Post with ID " + id + " deleted successfully.");
        return ResponseEntity.ok(successResponse);
    }
}
