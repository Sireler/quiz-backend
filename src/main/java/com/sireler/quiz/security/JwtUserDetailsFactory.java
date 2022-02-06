package com.sireler.quiz.security;

import com.sireler.quiz.model.User;

import java.util.Collections;

public class JwtUserDetailsFactory {

    public static JwtUserDetails create(User user) {
        return new JwtUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                true,
                Collections.emptyList()
        );
    }


}
