package com.example.DiceGameBE.utils;

import lombok.Getter;

@Getter
public enum MessageContents {

    GAME_ERROR_NOT_FOUND_OR_FINISHED("Game is not found or is already finished."),
    GAME_ERROR_BAD_OWNER("This is not your turn!");

    private final String content;
    MessageContents(String content) {
        this.content = content;
    }
}
