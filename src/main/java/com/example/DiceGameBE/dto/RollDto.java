package com.example.DiceGameBE.dto;


public record RollDto(int numberOfDicesToRoll, String gameId, boolean isFirstRollOnTurn) {

}
