package com.stranger.blogify.service;

import com.stranger.blogify.common.GlobalMethods;
import com.stranger.blogify.model.Question;
import com.stranger.blogify.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private GlobalMethods globalMethods;

   public List<Question> loadAllQuestions () {
       List<Question> allQuestions = questionRepository.findAll();
       allQuestions.sort(Comparator.comparing(Question::getCreatedDate));
       return allQuestions;
    }

    public Question addQuestion (Question question) {
        if(question!=null) {
            Question newQuestion = new Question();
            newQuestion.setId(globalMethods.createIdByTimeAndCategory(question.getDifficulty()));
            newQuestion.setName(question.getName());
            newQuestion.setQuestionLink(question.getQuestionLink());
            newQuestion.setDone(false);
            newQuestion.setDifficulty(question.getDifficulty());
            newQuestion.setSection(question.getSection());
            newQuestion.setCreatedDate(Instant.now());
            newQuestion.setUpdatedDate(null);
            questionRepository.save(newQuestion);
            return newQuestion;
        }
        return null;
    }
    public Optional<Question> getQuestionById(String id) {
        Optional<Question> question;
        if(null != id && !"".equals(id)) {
             question = questionRepository.findById(id);
            if (question.isPresent()){
                return question;
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
    public List<Question> getQuestionByDifficulty (String difficultyLevel) {
        if(null != difficultyLevel && !"".equals(difficultyLevel)) {
            return questionRepository.findByDifficulty(difficultyLevel);
        }
        return null;
    }

    public Question questionDone(Question question) {
       Optional<Question> optionalQuestion = questionRepository.findById(question.getId());
       if(optionalQuestion.isEmpty()) {
           return null;
       } else {
           Question updatedQuestion = optionalQuestion.get();
           updatedQuestion.setDone(question.isDone());
           questionRepository.save(updatedQuestion);
           return updatedQuestion;
       }
    }

    public List<Question>  addQuestion(List<Question> questionList) {
       if(questionList.isEmpty()) {
           return null;
       }
       List<Question> responseList = new ArrayList<>();
       for (Question question: questionList) {
           responseList.add(addQuestion(question));
       }
       return responseList;
    }
}
