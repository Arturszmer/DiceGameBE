package com.example.DiceGameBE.utils;

public enum GameplayContents implements MessageContents{

    NEXT_PLAYER("Next player is: %s"),
    DISCONNECT("%s disconnected this game"),
    CONNECT("New player is connected: %s");

    private final String content;

    GameplayContents(String content) {
        this.content = content;
    }

    @Override
    public String getContent(Object... param) {
        return String.format(content, param);
    }
}
