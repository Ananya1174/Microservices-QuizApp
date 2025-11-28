package com.example.quizapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.quizapp.model.Question;

public interface QuestionDao extends JpaRepository<Question,Integer>{

}
