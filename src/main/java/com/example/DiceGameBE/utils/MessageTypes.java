package com.example.DiceGameBE.utils;

import lombok.Getter;

@Getter
public enum MessageTypes {

    ERROR("error"),
    GAME_CREATED("game.created"),
    GAME_CONNECTED("game.connected"),
    GAME_RECONNECT("game.reconnect"),
    GAME_JOINED("game.joined"),
    GAME_LEAVE("game.leave"),
    GAME_ROLL("game.roll"),
    GAME_CHECK("game.check"),
    GAME_CLOSED("game.closed");

    private final String type;

    MessageTypes(String type) {
        this.type = type;
    }

}
