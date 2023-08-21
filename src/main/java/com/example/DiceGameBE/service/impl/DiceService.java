package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiceService {

    /*Metoda do losoania*/
    public List<Dice> rollDices(int numberOfDicesToRoll)  {
        Random random = new Random();
        int numberOfRoll = random.nextInt(1, 6);

        if (numberOfRoll > 5) throw new GameException(GameErrorResult.TOO_MANY_ROLLS);

        List<Dice> dices = new ArrayList<>();

        IntStream.range(0, numberOfRoll).map(i -> random.nextInt(6) + 1).forEach(value -> {
            boolean isGoodNumber = value == 1 || value == 5;
            Dice dice = new Dice(value, isGoodNumber, false, false, false);
            dices.add(dice);
        });

        addMultipleAttributes(dices);

        reduceRollsIfSpecialNumbers(dices);

        calculatePointsForMultiple(dices);

        return dices;
    }

/*metoda do dodania isMultiple*/
    private void addMultipleAttributes(List<Dice> dices) {
        Map<Integer, Integer> valueOccurrences = new HashMap<>();

        for (Dice dice : dices) {
            int value = dice.getValue();
            valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);
            boolean isMultiple = switch (valueOccurrences.get(value)) {
                case 3, 4, 5 -> true;
                default -> false;
            };

            dice.setMultiple(isMultiple);
        }
    }

    /* metoda do zmniejszenia ilości rzutów z 5 o ilość isGoodNumber ora isMultiple*/
    private void reduceRollsIfSpecialNumbers(List<Dice> dices) {
        long specialNumbersCount = dices.stream()
                .filter(Dice::isGoodNumber)
                .count();

        Map<Integer, Integer> valueOccurrences = new HashMap<>();

        for (Dice dice : dices) {
            int value = dice.getValue();
            valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);

            int multipleCount = valueOccurrences.get(value);

            if (multipleCount >= 3) {
                specialNumbersCount++;
            }
        }

        if (specialNumbersCount > 0) {
            long currentRolls = dices.size();
            long remainingRolls = 5 - currentRolls;
            long rollsToReduce = Math.min(remainingRolls, specialNumbersCount);

            dices.removeIf(Dice::isGoodNumber);

            IntStream.range(0, (int) rollsToReduce).forEach(i -> {
                int value = (Math.random() > 0.5) ? 1 : 5;
                dices.add(new Dice(value, true, false, false, false));
            });
        }
    }

    /*metoda do dodawania pkt*/
    public int calculatePointsForMultiple(List<Dice> dices) {
        Map<Integer, Integer> valueOccurrences = new HashMap<>();
        int totalPoints = 0;

        for (Dice dice : dices) {
            int value = dice.getValue();
            valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);

            int occurrences = valueOccurrences.get(value);
            int  multiplier = 2;

            int pointer1 =100;
            int pointer2 = 10;
            int pointer3 =0;

            switch (value) {
                case 1 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = pointer2;
                        case 3 -> totalPoints = pointer1;
                        case 4 -> totalPoints = pointer1*multiplier;
                        case 5 -> totalPoints = pointer1*multiplier*multiplier;
                    }
                }
                case 2, 3, 4, 6 -> totalPoints = getTotalPoints(totalPoints, value, occurrences, multiplier, pointer2, pointer3);

                case 5 -> totalPoints = getTotalPoints(totalPoints, value, occurrences, multiplier, pointer2, value);


                default -> {
                }
            }
        }

        return totalPoints;
    }
//metoda pomocnicza do metody dodające punkty
    private int getTotalPoints(int totalPoints, int value, int occurrences, int multiplier, int pointer2, int pointer3) {
        switch (occurrences) {
            case 1, 2 -> totalPoints = pointer3;
            case 3 -> totalPoints = pointer2*value;
            case 4 -> totalPoints = pointer2*value*multiplier;
            case 5 -> totalPoints = pointer2*value*multiplier*multiplier;
        }
        return totalPoints;
    }
}
//TODO zabezpieczyć przed null i 0
