package com.stranger.blogify.repository;

import com.stranger.blogify.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post, String> {
    Optional<Post> findBySlug(String slug);
}
