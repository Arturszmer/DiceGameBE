package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.model.Dice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiceService {

    public List<Dice> rollDice() {
        List<Dice> diceList = new ArrayList<>();
        Random random = new Random();

        IntStream
                .range(0, 5)
                .map(i -> random.nextInt(6) + 1)
                .forEach(value -> {
            boolean isGoodNumber = value == 1 || value == 5;
            boolean isMultiple = value == 3 || value == 6;
            Dice dice = new Dice(
                    value,
                    isGoodNumber,
                    false,
                    isMultiple,
                    false);
            diceList.add(dice);
        });
        return diceList;
    }

    public int sumDiceValues(List<Dice> diceList) {
        return diceList
                .stream()
                .mapToInt(Dice::getValue)
                .sum();
    }
}
