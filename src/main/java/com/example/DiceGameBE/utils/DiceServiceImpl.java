package com.example.DiceGameBE.utils;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.HashMapChangeSet;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {

    private final GameRepository repository;
    private final Random random = new Random();

    @Override
    public RollDicesResult rollDices(RollDto rollDto)  {
        RollDicesResult result = new RollDicesResult();
        Game game = repository.findById(rollDto.gameId())
                .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));
        if(rollDto.dices().isEmpty() && game.getDices().isEmpty()){
            getNumbersFromFirstRoll(result.getDices());
        } else {
            getNumbersFromRoll(result.getDices());
        }

        setAttributes(result.getDices());
        saveDicesToRedis(result.getDices());
        countMultiples(result.getDices());
        countAllGoodNumbers(result.getDices());
        countPoints(result.getDices());
        temporaryPoints(result.getDices());
        allPointsFromRoll(result.getDices());

        return result;
    }
    public void getNumbersFromFirstRoll(List<Dice> dices) {
        for (int i = 0; i < 5; i++) {
            int value = random.nextInt(6) +1;
            dices.add(new Dice(value));
        }
    }

    public void getNumbersFromRoll(List<Dice> dices) {
        int dicesToRoll = 5 - dices.size();

        List<Dice> noImmutableArrays = dices.stream()
                .filter(d -> !d.isImmutable())
                .toList();

        for (Dice dice : noImmutableArrays) {
            for (int i = 0; i < noImmutableArrays.size(); i++) {
                int value = random.nextInt(6) + 1;
                dices.add(new Dice(value));
            }
        }

        List<Dice> immutableArrays = dices.stream()
                .filter(d -> d.isImmutable() || d.isChecked() && !d.isGoodNumber() && !d.isMultiple())
                .toList();

        for (Dice dice : immutableArrays) {
            for (int i = 0; i < dicesToRoll; i++) {
                int value = random.nextInt(6) + 1;
                dices.add(new Dice(value));
            }
        }
    }

    public static int temporaryPoints(List<Dice> dices) {
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

    public static int allPointsFromRoll(List<Dice> dices) {
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

    public static int countPoints(List<Dice> dices) {
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

    public static int countAllGoodNumbers(List<Dice> dices) {
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

    public static int countMultiples(List<Dice> multiplesArray) {
        if (multiplesArray.get(0).getValue() != 1) {
            return multiplesArray.get(0).getValue() * 10 * (multiplesArray.size() - 2);
        } else {
            return 10 * (multiplesArray.size() - 2) * 10;
        }
    }
}
