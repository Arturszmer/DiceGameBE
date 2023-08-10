package com.example.DiceGameBE.DiceModelTest;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.service.GameBuilder;

import java.util.List;
import java.util.UUID;

public class DiceModel {


    private static final String GAME_ID = String.valueOf(UUID.randomUUID());
    private static final String ADMIN_PLAYER = "ADMIN_PLAYER";

    static Player ADMIN_PLAYER(){
        return new Player(0,ADMIN_PLAYER);}


    static Game builDiceModelGame(){
        return GameBuilder.aGameBuilder()
                .withGameId(GAME_ID)
                .withAdminPlayer(ADMIN_PLAYER())
                .withGameStatus(GameStatus.OPEN)
                .withPlayers(List.of(ADMIN_PLAYER()))
                .build();

    }

}






//TODO zrobimy pierwszego testa, rozbij sobie to zadanie na 2: 1. Stwórz branch, zrób model kostki i zrób pull requesta
