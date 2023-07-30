package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class DiceGameExceptionHandler {

//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(GameException.class)
    public ResponseEntity<ErrorResponse> handleGameException(final GameException exception){
        log.warn("Game exception occur: ", exception);
        return this.makeErrorResponse(exception.getErrorResult());
    }

    private ResponseEntity<ErrorResponse> makeErrorResponse(GameErrorResult errorResult) {
        return ResponseEntity.status(errorResult.getStatus()).body(new ErrorResponse(errorResult.name(), errorResult.getMessage()));
    }

    @Getter
    @AllArgsConstructor
    static class ErrorResponse{
        private String code;
        private String message;
    }
}
