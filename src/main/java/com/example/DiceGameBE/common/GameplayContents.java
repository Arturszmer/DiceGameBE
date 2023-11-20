package com.example.DiceGameBE.common;

public enum GameplayContents implements MessageContents{

    NEXT_PLAYER("Next player is: %s"),
    DISCONNECT("%s disconnected this game"),
    DISCONNECT_AND_CLOSE("The last player leaved this game, the game is finished by default"),
    CONNECT("New player is connected: %s"),

    WINNER("And the winner is: %s");

    private final String content;

    GameplayContents(String content) {
        this.content = content;
    }

    @Override
    public String getContent(Object... param) {
        return String.format(content, param);
    }
}
