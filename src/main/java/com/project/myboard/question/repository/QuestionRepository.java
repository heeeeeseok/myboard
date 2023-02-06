package com.project.myboard.question.repository;

import com.project.myboard.question.data.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> getByUserId(Long id);
}
