package com.example.DiceGameBE.dto;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RollDicesResult {

    private List<Dice> dices;
    private int temporaryPoints = 0;
    private int allPointsFromRoll = 0;
    private Player player;

    public RollDicesResult(List<Dice> dices) {
        this.dices = dices;
    }
}
