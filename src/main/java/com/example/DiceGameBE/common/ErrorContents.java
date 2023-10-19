package com.example.DiceGameBE.common;

public enum ErrorContents implements MessageContents {

    GAME_ERROR_NOT_FOUND_OR_FINISHED("Game nr id: %s is not found or is already finished."),
    GAME_ERROR_BAD_OWNER("This is not your turn mr/mrs %s!"),
    GAME_ERROR_GAME_NOT_EXIST("Game nr id: %s is not exist");

    private final String content;
    ErrorContents(String content) {
        this.content = content;
    }

    @Override
    public String getContent(Object... param) {
        return String.format(content, param);
    }
}
