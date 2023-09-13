 package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.HashMapChangeSet;
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
        saveDicesToRedis(dices);
        getMaxValueFromRoll(dices);
        getTemporaryPoints(dices);
        calculatePointsForMultiple(dices);
        countPoints(dices);

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
/*Metoda pomocnicza saveDicesToRedis*/
    private void saveDicesToRedis(List<Dice> dices){
        for (Dice dice :dices){
            String key = "dice: " + dice.getValue();
            String value = dice.toString();
            HashMapChangeSet redis = new HashMapChangeSet();
            redis.set(key,value);
        }
    }
/*Metoda przelicza maksymalna ilość punktów z danego wyrzutu*/
    public int getMaxValueFromRoll(List<Dice> dices) {
        int maxValueFromRoll = 0;
        for (Dice dice : dices) {
            int value = dice.getValue();
            if (value == 5) {
                maxValueFromRoll = 10;
            } else if (value > maxValueFromRoll) {
                maxValueFromRoll = value;
            }
        }
        return maxValueFromRoll;
        }
    /*Metoda zlicza punkty dla multiple*/
    private static int calculatePointsForMultiple (List<Dice> dices) {
        if (dices.get(0).getValue() != 1) {
            return dices.get(0).getValue() * 10 * (dices.size() - 2);
        } else {
            return 10 * (dices.size() - 2) * 10;
        }
    }

    /*Metoda zlicza punkty tylko z tych które mają na true atrybut isChecked*/
    public int getTemporaryPoints (List<Dice> dices){
        int temporaryPoints = 0;

        List<Dice> checkedArray = dices.stream()
                .filter( d -> d.isChecked() && !d.isImmutable() && !d.isGoodNumber() && !d.isMultiple())
                .toList();

        if (!checkedArray.isEmpty()) {
            temporaryPoints = calculatePointsForMultiple(checkedArray);
        }

        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple() && !d.isImmutable() && d.isChecked())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                temporaryPoints += 10;
            } else {
                temporaryPoints += 5;
            }
        }
        return temporaryPoints;
    }
    /*Metoda zlicza punkty*/
    public static int countPoints(List<Dice> dices) {
        int points = 0;

        List<Dice> multipleArray = dices.stream()
                .filter(d -> d.isMultiple()  &&!d.isImmutable() && !d.isChecked())
                .toList();

        if (!multipleArray.isEmpty()) {
            points = calculatePointsForMultiple(multipleArray);
        }
        List<Dice> restDices = dices.stream()
                .filter(d -> !d.isMultiple() && !d.isImmutable() && !d.isChecked())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                points += 10;
            } else {
                points += 5;
            }
        }
        List<Dice> multipleAndCheckedArray =  dices.stream()
                .filter(d-> d.isMultiple() && d.isChecked())
                .toList();

        if(!multipleAndCheckedArray.isEmpty()){
            points = calculatePointsForMultiple(multipleArray);
        }

        List<Dice> restDices2 = dices.stream()
                .filter(d-> !d.isMultiple() && d.isChecked())
                .toList();

        for (Dice dice : restDices2){
            if (dice.getValue() == 1){
                points +=10;
            } else
            {points += 5;
            }
        }

        return points;
    }
}
