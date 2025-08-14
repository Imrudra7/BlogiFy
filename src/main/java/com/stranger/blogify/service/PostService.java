package com.stranger.blogify.service;

import com.stranger.blogify.model.Post;
import com.stranger.blogify.repository.PostRepository;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Optional<Post> getPostById(String id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        String customId = generateCustomId(post.getTitle(), post.getAuthor());
        post.setId(customId);
        post.setSlug(generateSlug(post.getTitle()));
        post.setCreatedAt(Instant.now());
        //post.setUpdatedAt(Instant.now());
        post.setStatus("Draft");
        return postRepository.save(post);
    }
    private String generateSlug(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    private String generateCustomId(String title, String author) {
        long epochSeconds = Instant.now().getEpochSecond();
        String cleanTitle = title.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        String cleanAuthor = author.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        return cleanTitle + "-" + cleanAuthor + "-" + epochSeconds;
    }

    public void deletePostById(String id) {
         try {
             postRepository.deleteById(id);
             return;
         } catch (Exception e) {

         }
    }
}
