package com.example.quizapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quizapp.dao.QuestionDao;
import com.example.quizapp.model.Question;

@Service
public class QuestionService {
	@Autowired
	QuestionDao questionDao;

	public List<Question> getAllQuestions() {
		
		return questionDao.findAll();
	}

}
