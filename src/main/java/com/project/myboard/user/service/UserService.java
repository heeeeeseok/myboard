package com.project.myboard.user.service;

import com.project.myboard.answer.data.entity.Answer;
import com.project.myboard.answer.repository.AnswerRepository;
import com.project.myboard.configuration.security.AuthorityCheck;
import com.project.myboard.configuration.security.JwtTokenProvider;
import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import com.project.myboard.question.data.dto.QuestionDto;
import com.project.myboard.question.data.entity.Question;
import com.project.myboard.question.repository.QuestionRepository;
import com.project.myboard.question.service.QuestionService;
import com.project.myboard.user.data.dto.UserDto;
import com.project.myboard.user.data.entity.User;
import com.project.myboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AnswerRepository answerRepository;

    public void signUp(UserDto.SignUpDto signUpDto) {
        signUpDto.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        User user = signUpDto.toUserEntity();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new DataNotFoundException(e + "이미 존재하는 ID 입니다.");
        }
    }

    public UserDto.Response signIn(UserDto.SignInDto signInDto) {

        User user;

        Optional<User> optional = userRepository.findByUid(signInDto.getUid());
        if(optional.isPresent()) {
            user = optional.get();
        } else {
            throw new DataNotFoundException("존재하지 않는 ID 입니다.");
        }

        if(!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("비밀번호가 다릅니다.");
        }

        return UserDto.Response.builder()
                .uid(user.getUid())
                .name(user.getName())
                .role(user.getRole())
                .accessToken(jwtTokenProvider.createToken(user.getUid(), Collections.singletonList(user.getRole())))
                .build();
    }

    public void deleteUser(String requestUid, String deleteUid) {
        User requestUser = userRepository.getByUid(requestUid);

        Optional<User> optional = userRepository.findByUid(deleteUid);
        if(optional.isPresent()) {
            User deleteUser = optional.get();
            if(AuthorityCheck.isAccessible(requestUser, deleteUser)) {

                List<Question> deleteQuestionList = new ArrayList<>();
                deleteQuestionList = questionRepository.getByUserId(deleteUser.getId());
                for(Question question : deleteQuestionList) {
                    questionService.deleteQuestion(question.getId(), deleteUid);
                }

                List<Answer> deleteAnswerList = new ArrayList<>();
                deleteAnswerList = answerRepository.getByUserId(deleteUser.getId());
                for(Answer answer : deleteAnswerList) {
                    answerRepository.delete(answer);
                }

                userRepository.delete(deleteUser);
            } else {
                throw new BadRequestException("권한이 없습니다.");
            }
        } else {
            throw new DataNotFoundException("존재하지 않는 ID 입니다.");
        }
    }

    public List<QuestionDto.Response> getQuestionList(String uid) {
        User user = userRepository.getByUid(uid);
        List<Question> questionList = questionRepository.getByUserId(user.getId());
        List<QuestionDto.Response> questionResponseList = new ArrayList<>();
        for(Question question : questionList) {
            questionResponseList.add(QuestionDto.Response.builder()
                    .title(question.getTitle())
                    .content(question.getContent())
                    .createdBy(question.getCreatedBy())
                    .modifiedBy(question.getLastModifiedBy())
                    .createdAt(question.getCreatedAt())
                    .modifiedAt(question.getModifiedAt())
                    .build());
        }
        return questionResponseList;
    }
}
