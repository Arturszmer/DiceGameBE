package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;

import java.util.ArrayList;
import java.util.List;

public class DiceModels {

    public static List<Dice> allFalseDices(int... values){

        List<Dice> dices = new ArrayList<>();
        for (int value : values) {
            dices.add(DiceBuilder.aDiceBuilder()
                    .withValue(value)
                    .build());
        }
        return dices;
    }
}
