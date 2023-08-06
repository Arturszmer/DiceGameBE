package com.example.DiceGameBE.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GameErrorResult {

    GAME_NOT_FOUND_EX(HttpStatus.NOT_FOUND, "The game is not found"),
    GAME_PLAYERS_SIZE_EX(HttpStatus.BAD_REQUEST, "The maximum number of players in one game is 4"),
    ADD_PLAYER_TO_NOT_OPEN_GAME_EX(HttpStatus.BAD_REQUEST, "you cannot add players because game is finished"),
    UNIQUE_PLAYER_NAME_EX(HttpStatus.BAD_REQUEST, "The new player name is not unique in this game");

    private final HttpStatus status;
    private final String message;


}