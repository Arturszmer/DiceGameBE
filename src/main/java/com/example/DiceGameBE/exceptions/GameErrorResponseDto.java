package com.example.DiceGameBE.exceptions;

import org.springframework.http.HttpStatus;

public record GameErrorResponseDto(HttpStatus status, String message) {

}
