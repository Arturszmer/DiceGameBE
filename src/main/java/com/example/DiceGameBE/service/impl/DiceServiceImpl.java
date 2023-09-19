 package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

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

        //TODO: dodać funkcje liczącą punkty

        return result;
    }

     private void getNumbersFromFirstRoll(List<Dice> dices) {
            for (int i = 0; i < 5; i++) {
                int value = random.nextInt(6) +1;
                dices.add(new Dice(value));
            }
    }

     private void getNumbersFromRoll(List<Dice> dices) {
        // TODO: dodać filtry tak by pozostawić kostki zaznaczone ( i immutable), a rzucić wyłącznie pozostałymi koścmi
         // TODO: jeżeli wszystkie są immutabe lub checked to powinien rzucić znowu wszystkimi kostkami
         int dicesToRoll = 5 - dices.size();
         for (int i = 0; i < dicesToRoll; i++) {
             int value = random.nextInt(6) +1;
             dices.add(new Dice(value));
         }
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

