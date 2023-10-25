package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;

import java.util.List;
import java.util.UUID;

class GameModels {

    static final String GAME_ID = UUID.randomUUID()
            .toString().replace("-", "");
    private static final String ADMIN_NAME = "admin";

    static AdminPlayerDto createAdminPlayerDto() {
        return new AdminPlayerDto(0, ADMIN_NAME);
    }
    static Player createAdminPlayer(){
        return new Player(0, ADMIN_NAME);
    }

    static Game buildSimpleGame(){
        return GameBuilder.aGameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(createAdminPlayer())
                .withPlayers(List.of(createAdminPlayer()))
                .build();
    }
    static Game buildSimpleGame(String... players){
        Game game = GameBuilder.aGameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(createAdminPlayer())
                .withPlayers(List.of(createAdminPlayer()))
                .build();

        for (String s : players) {
            game.addPlayer(s);
        }

        return game;
    }

}
