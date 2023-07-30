package com.example.DiceGameBE.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GameErrorResult {

    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "The game is not found");

    private final HttpStatus status;
    private final String message;


}
