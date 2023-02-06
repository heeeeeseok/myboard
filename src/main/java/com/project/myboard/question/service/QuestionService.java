package com.project.myboard.question.service;

import com.project.myboard.answer.data.entity.Answer;
import com.project.myboard.answer.repository.AnswerRepository;
import com.project.myboard.configuration.security.AuthorityCheck;
import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import com.project.myboard.question.data.dto.QuestionDto;
import com.project.myboard.question.data.entity.Question;
import com.project.myboard.question.repository.QuestionRepository;
import com.project.myboard.user.data.entity.User;
import com.project.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;

    public QuestionDto.Response createQuestion(QuestionDto.QuestionInfo infoDto, String uid) {
        User user = userRepository.getByUid(uid);

        Question question = Question.builder()
                .title(infoDto.getTitle())
                .content(infoDto.getContent())
                .user(user)
                .build();
        questionRepository.save(question);


        return QuestionDto.Response.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .createdBy(question.getCreatedBy())
                .modifiedBy(question.getLastModifiedBy())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .build();
    }

    public QuestionDto.Response getQuestionDetail(Long id) {
        Optional<Question> optional = questionRepository.findById(id);
        Question question;
        if(optional.isPresent()) {
            question = optional.get();
        } else {
            throw new DataNotFoundException("해당 질문이 없습니다.");
        }

        return QuestionDto.Response.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .createdBy(question.getCreatedBy())
                .modifiedBy(question.getLastModifiedBy())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .build();
    }

    public QuestionDto.Response changeQuestionTitle(Long id, String newTitle, String uid) {
        Optional<Question> optional = questionRepository.findById(id);
        Question question;
        if(optional.isPresent()) {
            question = optional.get();
            if(AuthorityCheck.isAccessible(userRepository.getByUid(uid), question.getUser())) {
                question.setTitle(newTitle);
                questionRepository.save(question);
                question = questionRepository.getById(id);
            } else{
                throw new BadRequestException("권한이 없습니다.");
            }
        } else {
            throw new DataNotFoundException("해당 질문이 없습니다.");
        }

        return QuestionDto.Response.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .createdBy(question.getCreatedBy())
                .modifiedBy(question.getLastModifiedBy())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .build();
    }

    public QuestionDto.Response changeQuestionContent(Long id, String newContent, String uid) {
        Optional<Question> optional = questionRepository.findById(id);
        Question question;
        if(optional.isPresent()) {
            question = optional.get();
            if(AuthorityCheck.isAccessible(userRepository.getByUid(uid), question.getUser())) {
                question.setContent(newContent);
                questionRepository.save(question);
                question = questionRepository.getById(id);
            } else
                throw new BadRequestException("권한이 없습니다.");
        } else {
            throw new DataNotFoundException("해당 질문이 없습니다.");
        }

        return QuestionDto.Response.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .createdBy(question.getCreatedBy())
                .modifiedBy(question.getLastModifiedBy())
                .createdAt(question.getCreatedAt())
                .modifiedAt(question.getModifiedAt())
                .build();
    }

    public void deleteQuestion(Long id, String uid) {
        Optional<Question> optional = questionRepository.findById(id);
        Question question;
        if(optional.isPresent()) {
            question = optional.get();
            if(AuthorityCheck.isAccessible(userRepository.getByUid(uid), question.getUser())) {
                List<Answer> deletedAnswerList = new ArrayList<>();
                deletedAnswerList = answerRepository.getByQuestionId(question.getId());
                for(Answer answer : deletedAnswerList) {
                    answerRepository.delete(answer);
                }
                questionRepository.delete(question);
            } else
                throw new BadRequestException("권한이 없습니다.");
        } else {
            throw new DataNotFoundException("해당 질문이 없습니다.");
        }
    }
}
