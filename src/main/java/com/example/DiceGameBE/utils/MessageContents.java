package com.example.DiceGameBE.utils;

import lombok.Getter;

@Getter
public enum MessageContents {

    GAME_ERROR_NOT_FOUND_OR_FINISHED("Game is not found or is already finished.");

    private final String content;
    MessageContents(String content) {
        this.content = content;
    }
}
