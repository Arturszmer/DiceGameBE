package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;

import java.util.List;
import java.util.UUID;

public class models {

    public static final String GAME_ID = UUID.randomUUID()
            .toString().replace("-", "");
    private static final String ADMIN_NAME = "admin";

    static AdminPlayerDto createAdminPlayerDto() {
        return new AdminPlayerDto(0, ADMIN_NAME);
    }
    static Player createAdminPlayer(){
        return new Player(0, ADMIN_NAME);
    }

    static Game buildSimpleGame(){
        return GameBuilder.gameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(createAdminPlayer())
                .withPlayers(List.of(createAdminPlayer()))
                .build();
    }
    static Game buildSimpleGame(int numberOfPlayers){
        Game game = GameBuilder.gameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(createAdminPlayer())
                .withPlayers(List.of(createAdminPlayer()))
                .build();
        for(int i = 1; i <= numberOfPlayers; i++){
            game.getPlayers().add(new Player(i, "user" + i));
        }
        return game;
    }

}
