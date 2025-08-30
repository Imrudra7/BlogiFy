package com.stranger.blogify.repository;

import com.stranger.blogify.model.Question;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends MongoRepository<Question,String> {
    Optional<Question> findById(String id);
    Optional<Question> findByName(String name);
    List<Question> findByDone(boolean done);
    List<Question> findBySection(String section);
    List<Question> findByCreatedDate(Instant date);
    List<Question> findByDifficulty(String difficultyLevel);
}
