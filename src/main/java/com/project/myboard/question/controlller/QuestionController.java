package com.project.myboard.question.controlller;

import com.project.myboard.exception.BadRequestException;
import com.project.myboard.exception.DataNotFoundException;
import com.project.myboard.question.data.dto.QuestionDto;
import com.project.myboard.question.service.QuestionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/create")
    public ResponseEntity<QuestionDto.Response> createQuestion(
            HttpServletRequest request,
            @RequestBody QuestionDto.QuestionInfo infoDto) {
        String uid = String.valueOf(request.getAttribute("uid"));
        return ResponseEntity.status(HttpStatus.OK).body(questionService.createQuestion(infoDto, uid));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<QuestionDto.Response> getQuestion(@PathVariable Long id) {
        QuestionDto.Response question;
        try{
            question = questionService.getQuestionDetail(id);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/update/title/{id}")
    public ResponseEntity<QuestionDto.Response> updateQuestionTitle(
            HttpServletRequest request, Long id, String newTitle) {

        QuestionDto.Response question;
        String uid = String.valueOf(request.getAttribute("uid"));

        try{
            question = questionService.changeQuestionTitle(id, newTitle, uid);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/update/content/{id}")
    public ResponseEntity<QuestionDto.Response> updateQuestionContent(
            HttpServletRequest request, Long id, String newContent) {

        QuestionDto.Response question;
        String uid = String.valueOf(request.getAttribute("uid"));

        try{
            question = questionService.changeQuestionContent(id, newContent, uid);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(question);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "...",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(HttpServletRequest request, Long id) {

        String uid = String.valueOf(request.getAttribute("uid"));

        try{
            questionService.deleteQuestion(id, uid);
        } catch (DataNotFoundException e) {
            throw new DataNotFoundException(e.getMessage());
        } catch (BadRequestException e) {
            throw new BadRequestException(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("질문 및 답변이 정상적으로 삭제되었습니다.");
    }
}
