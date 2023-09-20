package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.HashMapChangeSet;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;


import static com.example.DiceGameBE.exceptions.GameErrorResult.BAD_AMOUNT_OF_DICES;

@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {


    @Override
    public List<Dice> rollDices(RollDto rollDto)  {
        validateDicesToRoll(rollDto.numberOfDicesToRoll());

        List<Dice> dices = getNumbersFromRoll(rollDto.numberOfDicesToRoll());

        setAttributes(dices);
        saveDicesToRedis(dices);
        countMultiples(dices);
        countAllGoodNumbers(dices);
        countPoints(dices);
        temporaryPoints(dices);
        allPointsFromRoll(dices);

        return dices;
    }

    public int temporaryPoints(List<Dice> dices) {
        int temporaryPoints =0;

        List<Dice> multiplesArray = dices.stream()
                .filter(d -> d.isMultiple() && d.isChecked())
                .collect(Collectors.toList());

        if (!multiplesArray.isEmpty()) {
            temporaryPoints = countMultiples(multiplesArray);
        }

        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple() && d.isChecked())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                temporaryPoints += 10;
            } else if (dice.getValue() == 5) {
                temporaryPoints += 5;
            }
        }
        return temporaryPoints;
    }

    public int allPointsFromRoll(List<Dice> dices) {
        int allPointsFromRoll =0;

        List<Dice> multiplesArray = dices.stream()
                .filter(Dice::isMultiple)
                .collect(Collectors.toList());

        if (!multiplesArray.isEmpty()) {
            allPointsFromRoll = countMultiples(multiplesArray);
        }

        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                allPointsFromRoll += 10;
            } else if (dice.getValue() == 5) {
                allPointsFromRoll += 5;
            }
        }
        return allPointsFromRoll;
    }

    private void validateDicesToRoll(int numberOfDicesToRoll) {
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

    private void saveDicesToRedis(List<Dice> dices){
        for (Dice dice :dices){
            String key = "dice: " + dice.getValue();
            String value = dice.toString();
            HashMapChangeSet redis = new HashMapChangeSet();
            redis.set(key,value);
        }
    }

    public int countPoints(List<Dice> dices) {
        int points = 0;

        List<Dice> multiplesArray = dices.stream()
                .filter(d -> d.isMultiple() && d.isChecked())
                .collect(Collectors.toList());

        if (!multiplesArray.isEmpty()) {
            points = countMultiples(multiplesArray);
        }

        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple() && d.isChecked() && !d.isImmutable())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                points += 10;
            } else {
                points += 5;
            }
        }

        return points;
    }

    public int countAllGoodNumbers(List<Dice> dices) {
        int points = 0;

        List<Dice> multiplesArray = dices.stream()
                .filter(d -> d.isMultiple() && !d.isImmutable() && !d.isChecked())
                .collect(Collectors.toList());

        if (!multiplesArray.isEmpty()) {
            points = countMultiples(multiplesArray);
        }

        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple() && !d.isImmutable() && !d.isChecked())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                points += 10;
            } else if (dice.getValue() == 5) {
                points += 5;
            }
        }

        return points;
    }

    public int countMultiples(List<Dice> multiplesArray) {
        if (multiplesArray.get(0).getValue() != 1) {
            return multiplesArray.get(0).getValue() * 10 * (multiplesArray.size() - 2);
        } else {
            return 10 * (multiplesArray.size() - 2) * 10;
        }
    }
}
