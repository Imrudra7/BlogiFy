package com.stranger.blogify.service;

import com.stranger.blogify.model.Post;
import com.stranger.blogify.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateService {
    private final PostRepository postRepository;

    public UpdateService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public boolean updatePost(String newValue, String id) {
        System.out.println("Inside Sevice: "+newValue+" "+id);
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isPresent()) {
            Post newPost = optionalPost.get();
            newPost.setContent(newValue);
            postRepository.save(newPost);
            return true;
        }else {
            return false;
        }
    }
}
