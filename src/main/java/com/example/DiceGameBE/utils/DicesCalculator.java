package com.example.DiceGameBE.utils;

import com.example.DiceGameBE.model.Dice;

import java.util.List;

public class DicesCalculator {


    public static int count(List<Dice> dices, int temporaryPoints) {

        int totalPoints = temporaryPoints;
        List<Dice> checked = dices.stream()
                .filter(dice -> dice.isGoodNumber() && dice.isChecked())
                .toList();

        totalPoints += countMultiple(checked);

        for(Dice dice : checked.stream().filter(dice -> !dice.isMultiple()).toList()){
            totalPoints += countPoints(dice.getValue());
        }

        return totalPoints;
    }

    public static int countPointsFromRoll(List<Dice> dices, int temporaryPoints){

        int totalPoints = temporaryPoints;
        List<Dice> toCount = dices.stream()
                .filter(Dice::isGoodNumber)
                .toList();

        totalPoints += countMultiple(toCount);

        for(Dice dice : toCount.stream().filter(dice -> !dice.isMultiple()).toList()){
            totalPoints += countPoints(dice.getValue());
        }
        return totalPoints;
    }

    private static int countPoints(int dice) {
        return switch (dice) {
            case 1 -> 10;
            case 5 -> 5;
            default -> 0;
        };
    }
    private static int countMultiple(List<Dice> dices) {
        List<Dice> multiples = dices.stream().filter(Dice::isMultiple).toList();
        int numberOfMultiples = multiples.size();

        if(numberOfMultiples < 3){
            return 0;
        } else {
            switch (multiples.get(0).getValue()) {
                case 1 -> {
                    return 100 * (numberOfMultiples - 2);
                }
                case 2 -> {
                    return 20 * (numberOfMultiples - 2);
                }
                case 3 -> {
                    return 30 * (numberOfMultiples - 2);
                }
                case 4 -> {
                    return 40 * (numberOfMultiples - 2);
                }
                case 5 -> {
                    return 50 * (numberOfMultiples - 2);
                }
                case 6 -> {
                    return 60 * (numberOfMultiples - 2);
                }
                default -> {
                    return 0;
                }
            }
        }
    }
}
