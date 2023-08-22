package com.example.DiceGameBE.service.impl;

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

import static com.example.DiceGameBE.exceptions.GameErrorResult.TOO_FEW_DICE_ROLLS;
import static com.example.DiceGameBE.exceptions.GameErrorResult.TOO_MANY_DICE_ROLLS;


@Service
@Slf4j
@RequiredArgsConstructor
public class DiceService {

    /*Metoda do losowania*/
    public List<Dice> rollDices(int numberOfDicesToRoll)  {
        Random random = new Random();

        if (numberOfDicesToRoll <= 0) throw new GameException(TOO_FEW_DICE_ROLLS);

        if (numberOfDicesToRoll > 5) throw new GameException(TOO_MANY_DICE_ROLLS);

        List<Dice> dices = new ArrayList<>();

        for (int i = 0; i < numberOfDicesToRoll; i++) {
            int value = random.nextInt(6) +1;
            boolean isGoodNumber = value == 1 || value == 5;
            Dice dice = new Dice(value, isGoodNumber, false, false, false);
            dices.add(dice);
        }

        calculateAttributes(dices);

        return dices;

    }
    /*metoda do dodawania isMultiple*/
    public void calculateAttributes(List<Dice> dices) {
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
}
