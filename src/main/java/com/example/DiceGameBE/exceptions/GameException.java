package com.example.DiceGameBE.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GameException extends RuntimeException {

    private final HttpStatus status;

    public GameException(GameErrorResult errorResult, Object ...o) {
        super(String.format(errorResult.getMessage(), o));
        this.status = errorResult.getStatus();
    }
}
