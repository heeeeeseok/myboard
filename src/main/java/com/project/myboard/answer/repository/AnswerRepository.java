package com.project.myboard.answer.repository;

import com.project.myboard.answer.data.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> getByQuestionId(Long id);
    List<Answer> getByUserId(Long id);
}
