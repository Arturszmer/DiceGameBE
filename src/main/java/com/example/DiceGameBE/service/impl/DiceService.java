package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.DiceGameBE.exceptions.GameErrorResult.MIN_NUMBER_OF_DICES_TO_ROLL;
import static com.example.DiceGameBE.exceptions.GameErrorResult.MAX_NUMBER_OF_DICES_TO_ROLL;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiceService {

    public List<Dice> rollDices(int numberOfDicesToRoll)  {
        validateDicesToRoll(numberOfDicesToRoll);

        List<Dice> dices = getNumbersFromRoll(numberOfDicesToRoll);

        setAttributes(dices);

        return dices;

    }

    private static void validateDicesToRoll(int numberOfDicesToRoll) {
        if (numberOfDicesToRoll <= 0) {
            throw new GameException(MIN_NUMBER_OF_DICES_TO_ROLL);
        }
        if (numberOfDicesToRoll > 5) {
            throw new GameException(MAX_NUMBER_OF_DICES_TO_ROLL);
        }
    }

    private List<Dice> getNumbersFromRoll(int numberOfDicesToRoll) {
        Random random = new Random();
        List<Dice> dices = new ArrayList<>();
        for (int i = 0; i < numberOfDicesToRoll; i++) {
            int value = random.nextInt(6) +1;
            dices.add(new Dice(value));
        }
        return dices;
    }

    private void setAttributes(List<Dice> dices) {
//       ROZWIĄZANIE NIEPOPRAWNE
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

//         ROZWIĄZANIE POPRAWNE
//        Map<Integer, Integer> diceValueCounts = new HashMap<>();
//        for (Dice dice : dices) {
//            int value = dice.getValue();
//            diceValueCounts.put(value, diceValueCounts.getOrDefault(value, 0) + 1);
//        }
//
//        for (Dice dice : dices) {
//            int value = dice.getValue();
//            int count = diceValueCounts.get(value);
//
//            if (count >= 3) {
//                dice.setMultiple(true);
//                dice.setGoodNumber(true);
//            }
//
//            if (value == 1 || value == 5) {
//                dice.setGoodNumber(true);
//            }
//        }
    }
}
