package com.example.DiceGameBE.dto;

import com.example.DiceGameBE.model.Dice;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RollDicesResult {

    private List<Dice> dices = new ArrayList<>();
    private int temporaryPoints = 0;
    private int allPointsFromRoll = 0;
}
