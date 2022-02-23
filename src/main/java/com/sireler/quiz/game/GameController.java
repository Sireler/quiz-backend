package com.sireler.quiz.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    private GameRepository gameRepository;

    @Autowired
    public GameController(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @MessageMapping("/game.{gameId}.join")
    public void join(@Payload GameMessage gameMessage, @DestinationVariable("gameId") Long gameId) {
        Game game = gameRepository.getGame(gameId);
        if (game != null) {
            game.join(gameMessage.getMessage());
        }
    }

    @MessageMapping("/game.{gameId}")
    @SendTo("/topic/game.{gameId}")
    public Game game(@Payload GameMessage gameMessage, @DestinationVariable("gameId") Long gameId) {
        return gameRepository.getGame(1L);
    }
}
