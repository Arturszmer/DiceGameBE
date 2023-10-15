package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsTests {

    public static List<Dice> setDicesAttributes(List<Dice> dices) {
        List<Dice> dicesToManageAttributes = dices.stream().filter(dice -> !dice.isImmutable()).toList();
        Map<Integer, Integer> diceValueCounts = new HashMap<>();
        for (Dice dice : dicesToManageAttributes) {
            int value = dice.getValue();
            diceValueCounts.put(value, diceValueCounts.getOrDefault(value, 0) + 1);
        }

        for (Dice dice : dicesToManageAttributes) {
            int value = dice.getValue();
            int count = diceValueCounts.get(value);

            if (count >= 3) {
                dice.setMultiple(true);
                dice.setGoodNumber(true);
            }

            if (value == 1 || value == 5) {
                dice.setGoodNumber(true);
            }
        }
        return dicesToManageAttributes;
    }
}
