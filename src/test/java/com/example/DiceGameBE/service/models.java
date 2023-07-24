package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;

import java.util.List;
import java.util.UUID;

public class models {

    static final String GAME_ID = UUID.randomUUID()
            .toString().replace("-", "");
    private static final String ADMIN_NAME = "admin";

    static CreatePlayerDto createAdminPlayerDto() {
        return new CreatePlayerDto(0, ADMIN_NAME);
    }
    static Player createAdminPlayer(){
        return new Player(0, ADMIN_NAME);
    }
    static CreatePlayerDto createSimplePlayerDto(int id, String name) {
        return new CreatePlayerDto(id, name);
    }
    static Player createSimplePlayer(int id, String name){
        return new Player(id, name);
    }

    static Game buildSimpleGame(){
        return GameBuilder.aGameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(createAdminPlayer())
                .withPlayers(List.of(createAdminPlayer()))
                .build();
    }
    static Game buildSimpleGame(int numberOfPlayers){
        Game game = GameBuilder.aGameBuilder()
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
