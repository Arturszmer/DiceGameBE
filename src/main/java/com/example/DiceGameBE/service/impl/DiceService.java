package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiceService {

    public List<Dice> rollDices() throws GameException {
        Random random = new Random();
        int numberOfRepeats = 3;
        int numberOfRolls = random.nextInt(5) + 1;

        if (numberOfRolls > 5) {
            throw new GameException(GameErrorResult.valueOf("Number of rolls cannot > 5."));
        }
        List<Dice> diceList = new ArrayList<>();
        Random random2 = new Random();

        Map<Integer, Integer> valueOccurrences = new HashMap<>();

        IntStream.range(0, numberOfRolls)
                .map(i -> random2.nextInt(6) + 1)
                .forEach(value -> {
                    boolean isGoodNumber = value == 1 || value == 5;
                    valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);
                    boolean isMultiple = valueOccurrences.getOrDefault(value, 0) >= numberOfRepeats;
                    Dice dice = new Dice(value, isGoodNumber, false, isMultiple, false);
                    diceList.add(dice);
                    if (valueOccurrences.get(value) >= numberOfRepeats) {
                        dice.setMultiple(true);
                    }
                });
        return diceList;
    }
}
