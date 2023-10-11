package com.example.DiceGameBE.controller.rest;

import com.example.DiceGameBE.exceptions.GameErrorResponseDto;
import com.example.DiceGameBE.exceptions.GameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class DiceGameExceptionHandler {

    @ExceptionHandler(GameException.class)
    public ResponseEntity<GameErrorResponseDto> handleGameException(final GameException exception){
        log.warn("Game exception occur: ", exception);
        return ResponseEntity.status(exception.getStatus()).body(
                new GameErrorResponseDto(exception.getStatus(), exception.getMessage()));
    }
}
