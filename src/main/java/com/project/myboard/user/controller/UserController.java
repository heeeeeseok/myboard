package com.project.myboard.user.controller;

import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import com.project.myboard.question.data.dto.QuestionDto;
import com.project.myboard.user.data.dto.UserDto;
import com.project.myboard.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.parser.Entity;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp (
            @RequestBody UserDto.SignUpDto signUpDto) {
        try {
            userService.signUp(signUpDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("회원가입 완료");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> signIn(
            @RequestBody UserDto.SignInDto signInDto) {
        UserDto.Response response;
        try {
            response = userService.signIn(signInDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response.toString());
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/user/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request, String deleteUid) {
        String requestUid = String.valueOf(request.getAttribute("uid"));

        try {
            userService.deleteUser(requestUid, deleteUid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("작성한 질문과 답변, 아이디가 삭제되었습니다.");
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/user/questions")
    public ResponseEntity<String> getQuestionList(HttpServletRequest request) {
        String uid = String.valueOf(request.getAttribute("uid"));
        List<QuestionDto.Response> responseList = userService.getQuestionList(uid);
        StringBuilder stringBuilder = new StringBuilder();
        for (QuestionDto.Response response : responseList) {
            stringBuilder.append(response.toString() + "\n");
        }
        return ResponseEntity.status(HttpStatus.OK).body(stringBuilder.toString());
    }
}
