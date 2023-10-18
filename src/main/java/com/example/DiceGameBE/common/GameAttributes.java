package com.example.DiceGameBE.common;

import lombok.Getter;

@Getter
public enum GameAttributes {

    GAME_ID("gameId"),
    PLAYER("player");

    private final String name;

    GameAttributes(String name) {
        this.name = name;
    }
}
