package com.quiz.service;


import com.quiz.dao.QuizDao;
import com.quiz.feign.QuizInterface;
import com.quiz.model.QuestionWrapper;
import com.quiz.model.Quiz;
import com.quiz.model.Response;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;
    
    private static final String CB_NAME = "questionServiceCB";


    @CircuitBreaker(name = CB_NAME, fallbackMethod = "createQuizFallback")
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

        List<Integer> questions = quizInterface.getQuestionsForQuiz(category, numQ).getBody();

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> createQuizFallback(String category, int numQ, String title, Throwable ex) {
        System.out.println("Fallback createQuiz: " + ex.getMessage());
        return new ResponseEntity<>("Question Service is Down! Try later.", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
          Quiz quiz = quizDao.findById(id).get();
          List<Integer> questionIds = quiz.getQuestionIds();
          ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);
          return questions;

    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
    }
}
