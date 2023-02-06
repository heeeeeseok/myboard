package com.project.myboard.configuration.security;

import com.project.myboard.user.data.entity.User;

public class AuthorityCheck {

    public static boolean isAccessible(User requestUser, User OriginUser) {
        return (requestUser.getRole().equals("ADMIN") ||
                requestUser.getUid().equals(OriginUser.getUid()));
    }

}
