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
//TODO widze ze jest redis, ale redis jako baza potrzebuje klucza i wartości, czy moge dodac autogenerate id do modelu, czy pomysł jest jakiś inny/
        saveDicesToRedis(dices);
        getAllPointsFromRoll(dices);
        getTemporaryPoints(dices);
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
            //TODO czy w Dice nie powinno byc id, aby w redisie była para klucz wartość
            String value = dice.toString();
            HashMapChangeSet redis = new HashMapChangeSet();
            redis.set(key,value);
        }
    }
/*Metoda przelicza maksymalna ilość punktór w danego wyrzutu*/
    public int getAllPointsFromRoll (List<Dice> dices){
           int allPointsFromRoll = 0;
           for(Dice dice : dices){
                   int value = dice.getValue();
                   allPointsFromRoll += value;
           }
    return allPointsFromRoll;
    }
/*Metoda zlicza punkty tylko z tych które mają na true atrybut isChecked*/
    public int getTemporaryPoints (List<Dice> dices){
        int temporaryPoints = 0;
        for(Dice dice : dices){
            if(dice.isChecked()){
                int value = dice.getValue();
                temporaryPoints += value;
            }
        }
        return temporaryPoints;
    }

    /*metoda do dodawania pkt, wiem że można ją uprościć ale tak jest dla mnie bardziej czytelna w następnej fazie ją skróce ale nie jest jeszcze dokończona*/
    public int calculatePointsForMultiple(List<Dice> dices) {
        Map<Integer, Integer> valueOccurrences = new HashMap<>();
        int totalPoints = 0;

        int value;
        for (Dice dice : dices) {
            value = dice.getValue();
            valueOccurrences.put(value, valueOccurrences.getOrDefault(value, 0) + 1);

            int occurrences = valueOccurrences.get(value);
            int multiplier = 2;

            int points1 = 100;
            int points2 = 10;
            int points3 = 0;
            int points4 = 20;
            int points5 = 30;
            int points6 = 40;
            int points7 = 50;
            int points8 = 60;
            int points9 = 5;


            switch (value) {
                /*case -> wartość wyrzuconej cyfry na kostce max 6*/
                case 1 -> {
                    /*dla powtórzeń max 5*/
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points2;
                        case 3 -> totalPoints = points1;
                        case 4 -> totalPoints = points1 * multiplier;
                        case 5 -> totalPoints = points1 * multiplier * multiplier;
                    }
                }
                case 2 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points3;
                        case 3 -> totalPoints = points4;
                        case 4 -> totalPoints = points4 * multiplier;
                        case 5 -> totalPoints = points4 * multiplier * multiplier;
                    }
                }
                case 3 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points3;
                        case 3 -> totalPoints = points5;
                        case 4 -> totalPoints = points5 * multiplier;
                        case 5 -> totalPoints = points5 * multiplier * multiplier;
                    }
                }
                case 4 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points3;
                        case 3 -> totalPoints = points6;
                        case 4 -> totalPoints = points6 * multiplier;
                        case 5 -> totalPoints = points6 * multiplier * multiplier;
                    }
                }
                case 5 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points9;
                        case 3 -> totalPoints = points7;
                        case 4 -> totalPoints = points7 * multiplier;
                        case 5 -> totalPoints = points7 * multiplier * multiplier;
                    }
                }
                case 6 -> {
                    switch (occurrences) {
                        case 1, 2 -> totalPoints = points3;
                        case 3 -> totalPoints = points8;
                        case 4 -> totalPoints = points8 * multiplier;
                        case 5 -> totalPoints = points8 * multiplier * multiplier;
                    }
                }
                default -> {
                }
            }
        }
            return totalPoints;
        }
}
//TODO metoda calculate nie jest dokończona nie działa jeszcze w przypadku 1 i 5
