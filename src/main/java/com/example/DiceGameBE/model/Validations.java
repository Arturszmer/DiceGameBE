package com.example.DiceGameBE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Validations {
    private boolean isNextPlayer;
    private boolean isRolling;
    private boolean isSaved;
    private boolean isWinner;
}
