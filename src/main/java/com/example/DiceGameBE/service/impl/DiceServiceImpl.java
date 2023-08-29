 package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import static com.example.DiceGameBE.exceptions.GameErrorResult.BAD_AMOUNT_OF_DICES;

@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {

    @Override
    public List<Dice> rollDices(int numberOfDicesToRoll)  {
        validateDicesToRoll(numberOfDicesToRoll);

        List<Dice> dices = getNumbersFromRoll(numberOfDicesToRoll);

        setAttributes(dices);

        return dices;
    }

    private static void validateDicesToRoll(int numberOfDicesToRoll) {
        if (numberOfDicesToRoll <= 0 || numberOfDicesToRoll >5) {
            throw new GameException(BAD_AMOUNT_OF_DICES);
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
        Map<Integer, Integer> diceValueCounts = new HashMap<>();
        for (Dice dice : dices) {
            int value = dice.getValue();
            diceValueCounts.put(value, diceValueCounts.getOrDefault(value, 0) + 1);
        }

        for (Dice dice : dices) {
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
    }
}

