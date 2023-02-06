package com.project.myboard.question.data.dto;

import lombok.*;

public class QuestionDto {

    @Data
    @Builder
    public static class QuestionInfo {

        private String title;
        private String content;

    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        private String title;
        private String content;
        private String createdBy;
        private String modifiedBy;
        private String createdAt;
        private String modifiedAt;

        @Override
        public String toString() {
            return "{\n"
                    + "\ttitle : " + title + "\n"
                    + "\tcontent : " + content + "\n"
                    + "\tcreatedBy : " + createdBy + "\n"
                    + "\tmodifiedBy : " + modifiedBy + "\n"
                    + "\tcreatedAt : " + createdAt + "\n"
                    + "\tmodifiedAt : " + modifiedAt + "\n"
                    + "}";
        }
    }
}
