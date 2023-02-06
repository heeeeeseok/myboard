package com.project.myboard.user.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.myboard.user.data.entity.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

public class UserDto {

    @Data
    @Builder
    public static class AuthenticationDto {
        private String uid;
        private String password;
        private List<String> roles;
    }

    @Data
    @Builder
    public static class SignUpDto {
        private String uid;
        private String password;
        private String name;
        private String role;

        public User toUserEntity() {
            if(role.equalsIgnoreCase("ADMIN")) {
                return User.builder()
                        .uid(uid)
                        .password(password)
                        .name(name)
                        .role("ADMIN")
                        .build();
            } else
                return User.builder()
                        .uid(uid)
                        .password(password)
                        .name(name)
                        .role("USER")
                        .build();

        }
    }

    @Data
    @Builder
    public static class SignInDto {
        private String uid;
        private String password;
    }

    @Data
    @Builder
    public static class ChangePasswordDto {
        private String currentPassword;
        private String newPassword;
    }

    @Data
    @Builder
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    public static class Response {
        private String uid;
        private String name;
        private String role;
        private String accessToken;
    }
}
