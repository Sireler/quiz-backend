package com.sireler.quiz.game;

import java.util.HashMap;
import java.util.Map;

public class GameRepository {

    public GameRepository() {
        Game game = new Game(1L);
        games.put(1L, game);
    }

    private Map<Long, Game> games = new HashMap<>();

    public void add(Long gameId, Game game) {
        games.put(gameId, game);
    }

    public Game getGame(Long gameId) {
        return games.get(gameId);
    }

    public void removeGame(Long gameId) {
        games.remove(gameId);
    }

    public Map<Long, Game> getGames() {
        return games;
    }
}
