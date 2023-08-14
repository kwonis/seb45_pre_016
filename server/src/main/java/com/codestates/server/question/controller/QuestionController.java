package com.codestates.server.question.controller;

import com.codestates.server.question.dto.QuestionPatchDto;
import com.codestates.server.question.dto.QuestionPostDto;
import com.codestates.server.question.dto.QuestionResponseDto;
import com.codestates.server.question.entity.Question;
import com.codestates.server.question.mapper.QuestionMapper;
import com.codestates.server.question.service.QuestionService;
import com.codestates.server.question.uri.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final QuestionMapper mapper;


    /**
     * user 등록
     * @param questionPostDto
     * @return
     */
    @PostMapping("/ask")
    public ResponseEntity postQuestion(@RequestBody QuestionPostDto questionPostDto){
        Long userId = questionPostDto.getUserId();
        Question question = questionService.createQuestion(mapper.questionPostDtoToQuestion(questionPostDto));

        URI location = UriCreator.createUri("/questions", question.getQuestionId());

        return ResponseEntity.created(location).build();
    }

    /**
     * user 수정
     * @param questionsId
     * @param questionPatchDto
     * @return
     */
    @PatchMapping("/{question-id}")
    public ResponseEntity patchQuestion(@PathVariable("question-id") Long questionsId,
                                @RequestBody QuestionPatchDto questionPatchDto) {
        questionPatchDto.setQuestionId(questionsId); //question id set
        Question question = mapper.questionPatchDtoToQuestion(questionPatchDto);
        Question updatedQuestion = questionService.updateQuestion(question);
        URI location = UriCreator.createUri("/questions", question.getQuestionId());
        return ResponseEntity.created(location).build();
    }

    /**
     * user 검색
     * @param questionsId
     * @return
     */
    @GetMapping("/{question-id}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable("question-id") Long questionsId){
        Question question = questionService.findQuestion(questionsId);
        QuestionResponseDto questionResponseDto = mapper.questionToQuestionResponseDto(question);
        return new ResponseEntity<>(questionResponseDto,HttpStatus.OK);
    }

    /**
     * 전체 검색
     * { username : 정해영
     *   title : 제목,
     *   modifiedAt : 날짜,
     *   {tag , tag , tag , tag , tag}
     *   }
     */
    @GetMapping
    public ResponseEntity<List<QuestionResponseDto>> getQuestions(){

        List<Question> allQuestions = questionService.findAllQuestions();
        List<QuestionResponseDto> questionResponseDtoList = allQuestions.stream().map(q->mapper.questionToQuestionResponseDto(q)).collect(Collectors.toList());
        return new ResponseEntity<>(questionResponseDtoList, HttpStatus.OK);
    }

    /**
     * 유저 삭제
     * @param questionId
     * @return
     */
    @DeleteMapping("/{question-id}")
    public ResponseEntity deleteQuestion(@PathVariable("question-id") Long questionId){

        questionService.deleteQuestion(questionId);

        return new ResponseEntity(HttpStatus.OK); //반환값 수정
    }
}