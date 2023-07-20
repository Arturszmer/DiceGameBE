package com.example.DiceGameBE.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Validations implements Serializable {
    private boolean isNextPlayer;
    private boolean isRolling;
    private boolean isSaved;
    private boolean isWinner;
}
