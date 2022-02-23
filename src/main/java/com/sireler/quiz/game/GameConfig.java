package com.sireler.quiz.game;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class GameConfig {

    @Bean
    @Description("Keeps created games")
    public GameRepository gameRepository() {
        return new GameRepository();
    }
}
