package com.sireler.quiz.service;

import com.sireler.quiz.model.User;

public interface UserService {

    User findByUsername(String username);

    User register(User user);

    String getToken(String username, String password);
}
