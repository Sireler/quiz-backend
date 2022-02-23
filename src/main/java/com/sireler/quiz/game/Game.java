package com.sireler.quiz.game;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Game {

    private Long id;

    private boolean started = false;

    private List<String> usernames = new ArrayList<>();

    public Game(Long id) {
        this.id = id;
    }

    public void join(String username) {
        usernames.add(username);
    }

    public void start() {
        started = true;
    }
}
