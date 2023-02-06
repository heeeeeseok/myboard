package com.project.myboard.answer.data.dto;

import lombok.*;

public class AnswerDto {

    @Data
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class AnswerInfo {

        private String content;

    }

    @Data
    @Builder
    public static class Response {

        private String content;
        private String createdBy;
        private String ModifiedBy;
        private String createdAt;
        private String modifiedAt;
    }
}
