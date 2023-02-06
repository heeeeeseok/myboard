package com.project.myboard.answer.service;

import com.project.myboard.answer.data.dto.AnswerDto;
import com.project.myboard.answer.data.entity.Answer;
import com.project.myboard.answer.repository.AnswerRepository;
import com.project.myboard.configuration.security.AuthorityCheck;
import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import com.project.myboard.question.data.entity.Question;
import com.project.myboard.question.repository.QuestionRepository;
import com.project.myboard.user.data.entity.User;
import com.project.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public AnswerDto.Response createAnswer(AnswerDto.AnswerInfo infoDto, Long questionId, String uid) {
        User user = userRepository.getByUid(uid);
        Question question;
        try {
            question = questionRepository.getById(questionId);
        } catch (Exception e) {
            throw new DataNotFoundException("해당 질문이 없습니다.");
        }

        Answer answer = Answer.builder()
                .content(infoDto.getContent())
                .question(question)
                .user(user).build();
        answerRepository.save(answer);

        return AnswerDto.Response.builder()
                .content(answer.getContent())
                .createdBy(answer.getCreatedBy())
                .ModifiedBy(answer.getLastModifiedBy())
                .createdAt(answer.getCreatedAt())
                .modifiedAt(answer.getModifiedAt())
                .build();
    }

    public AnswerDto.Response changeAnswerContent(Long answerId, String uid, String newContent) {
        User user = userRepository.getByUid(uid);
        Optional<Answer> optional = answerRepository.findById(answerId);
        Answer answer;
        if(optional.isPresent()) {
            answer = optional.get();
            if(AuthorityCheck.isAccessible(user, answer.getUser())) {
                answer.setContent(newContent);
                answerRepository.save(answer);
            } else {
                throw new BadRequestException("권한이 없습니다.");
            }
        } else {
            throw new DataNotFoundException("해당 답변이 없습니다.");
        }

        return AnswerDto.Response.builder()
                .content(answer.getContent())
                .createdBy(answer.getCreatedBy())
                .ModifiedBy(answer.getLastModifiedBy())
                .createdAt(answer.getCreatedAt())
                .modifiedAt(answer.getModifiedAt())
                .build();
    }

    public void deleteAnswer(Long answerId, String uid) {
        User user = userRepository.getByUid(uid);
        Optional<Answer> optional = answerRepository.findById(answerId);
        Answer answer;
        if(optional.isPresent()) {
            answer = optional.get();
            if(AuthorityCheck.isAccessible(user, answer.getUser())) {
                answerRepository.delete(answer);
            } else {
                throw new BadRequestException("권한이 없습니다.");
            }
        } else {
            throw new DataNotFoundException("해당 답변이 없습니다.");
        }
    }
}
