package com.example.DiceGameBE.utils;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;

public class GameValidations {

    public static boolean inputInvalid(Game game) {
        return game.getGameStatus() != GameStatus.FINISHED;
    }

    public static boolean gameOwnerInvalid(Game game, String owner){
        return !game.getCurrentPlayer().getName().equals(owner);
    }
}
