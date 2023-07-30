package com.example.DiceGameBE.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GameException extends RuntimeException {

    private final GameErrorResult errorResult;


}
