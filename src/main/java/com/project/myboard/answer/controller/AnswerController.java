package com.project.myboard.answer.controller;

import com.project.myboard.answer.data.dto.AnswerDto;
import com.project.myboard.answer.service.AnswerService;
import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;
    private final Logger LOGGER = LoggerFactory.getLogger(AnswerController.class);

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    public ResponseEntity<AnswerDto.Response> createAnswer(
            HttpServletRequest request,
            Long questionId,
            @RequestBody AnswerDto.AnswerInfo infoDto) {
        String uid = String.valueOf(request.getAttribute("uid"));
        return ResponseEntity.status(HttpStatus.OK).body(answerService.createAnswer(infoDto, questionId, uid));
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/update/content")
    public ResponseEntity<AnswerDto.Response> changeAnswerContent(
            HttpServletRequest request,
            Long answerId,
            String newContent) {
        String uid = String.valueOf(request.getAttribute("uid"));
        AnswerDto.Response response;
        try {
            response = answerService.changeAnswerContent(answerId, uid, newContent);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteAnswer(
            HttpServletRequest request,
            Long answerId) {
        String uid = String.valueOf(request.getAttribute("uid"));
        try {
            answerService.deleteAnswer(answerId, uid);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("답변이 정상적으로 삭제되었습니다.");
    }
}
