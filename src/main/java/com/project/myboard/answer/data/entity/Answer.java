package com.project.myboard.answer.data.entity;

import com.project.myboard.BaseEntity.BaseTimeAndEditorEntity;
import com.project.myboard.question.data.entity.Question;
import com.project.myboard.user.data.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseTimeAndEditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Question question;

}
