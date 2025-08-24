package com.stranger.blogify.repository;

import com.stranger.blogify.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName (String id);
    Optional<User> findByEmail (String email);
}
