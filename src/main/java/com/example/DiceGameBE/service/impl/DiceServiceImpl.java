 package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Validations;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.DiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import static com.example.DiceGameBE.dto.message.MessageMapper.*;
import static com.example.DiceGameBE.utils.MessageContents.*;
import static com.example.DiceGameBE.utils.MessageTypes.*;

@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {

     private final GameRepository repository;
     private final Random random = new Random();


    @Override
    public RollDicesResult rollDices(RollDto rollDto)  {

        RollDicesResult result = new RollDicesResult(rollDto.dices());

        Game game = repository.findById(rollDto.gameId())
                .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

        if(result.getDices().isEmpty() && game.getDices().isEmpty()){
            getNumbersFromFirstRoll(result.getDices());

            //TODO: oblczanie punktów dla całego rzutu

        } else {
            //TODO: obliczenia punktów dla isChecked
            getNumbersFromRoll(result.getDices());
        }

        checkPossibilityToNextRoll(result.getDices(), game);

        //TODO: dodać funkcje liczącą punkty
        game.setDices(result.getDices());
        result.setPlayer(game.getCurrentPlayer());
        repository.save(game);

        return result;
    }

     @Override
     public GameMessage rollDices(DiceMessage message) {

         Optional<Game> gameOpt = repository.findById(message.getGameId());

         if(gameOpt.isEmpty() || gameOpt.get().getGameStatus() == GameStatus.FINISHED){
             return MessageMapper.errorMessage();
         }

         Game game = gameOpt.get();
         List<Dice> dices = message.getDices();

         if(dices.isEmpty()){
             getNumbersFromFirstRoll(dices);
             checkPossibilityToNextRoll(dices, game);
         } else {
             getNumbersFromRoll(dices);
             checkPossibilityToNextRoll(dices, game);
         }
         game.setDices(dices);
         repository.save(game);

         return gameToMessage(game, GAME_ROLL);
     }

     @Override
     public RollDicesResult checkDices(RollDto rollDto) {
         RollDicesResult result = new RollDicesResult(rollDto.dices());
         Game game = repository.findById(rollDto.gameId())
                 .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

         checkPossibilityToNextRoll(result.getDices(), game);
         //TODO: obliczenie temporary points
         game.setDices(result.getDices());
         result.setPlayer(game.getCurrentPlayer());
         repository.save(game);

        return result;
     }

     @Override
     public GameMessage checkDices(DiceMessage message) {
         Optional<Game> gameOpt = repository.findById(message.getGameId());
         if(gameOpt.isEmpty() || gameOpt.get().getGameStatus() == GameStatus.FINISHED){
             return GameMessage.builder()
                     .type(ERROR.getType())
                     .content(GAME_ERROR_NOT_FOUND_OR_FINISHED.name())
                     .build();
         }
         Game game = gameOpt.get();
         List<Dice> dices = message.getDices();

         checkPossibilityToNextRoll(dices, game);

         game.setDices(dices);
         repository.save(game);

         GameMessage gameMessage = gameToMessage(game);
         gameMessage.setType(GAME_CHECK.getType());

         return gameMessage;
     }

     private void checkPossibilityToNextRoll(List<Dice> dices, Game game) {

         Validations validations = game.getCurrentPlayer().getValidations();
         boolean canRoll = !dices.stream().filter(dice -> dice.isGoodNumber() && !dice.isImmutable()).toList().isEmpty();
         validations.setRolling(canRoll);
         validations.setNextPlayer(!canRoll);
     }

     private void getNumbersFromFirstRoll(List<Dice> dices) {
            for (int i = 0; i < 5; i++) {
                int value = random.nextInt(6) +1;
                dices.add(new Dice(value));
            }
            setAttributes(dices);
    }

     private void getNumbersFromRoll(List<Dice> dices) {
        // TODO: dodać filtry tak by pozostawić kostki zaznaczone ( i immutable), a rzucić wyłącznie pozostałymi koścmi
         // TODO: jeżeli wszystkie są immutabe lub checked to powinien rzucić znowu wszystkimi kostkami
         if(dices.stream().filter(dice -> dice.isChecked() || dice.isImmutable()).toList().size() == 5){
             for (int i = 0; i < 5; i++) {
                 int value = random.nextInt(6) +1;
                 dices.get(i).insertNewValueFromRoll(value);
             }
         } else {
             for (int i = 0; i < 5; i++) {
                 Dice dice = dices.get(i);
                 if(!(dice.isChecked() || dice.isImmutable())){
                     int value = random.nextInt(6) +1;
                     dice.insertNewValueFromRoll(value);
                 }
                 if(dice.isChecked() && !dice.isImmutable()){
                     dice.setImmutable(true);
                 }
             }
         }
         setAttributes(dices);
     }

    private void setAttributes(List<Dice> dices) {
        List<Dice> dicesToManageAttributes = dices.stream().filter(dice -> !dice.isImmutable()).toList();
        Map<Integer, Integer> diceValueCounts = new HashMap<>();
        for (Dice dice : dicesToManageAttributes) {
            int value = dice.getValue();
            diceValueCounts.put(value, diceValueCounts.getOrDefault(value, 0) + 1);
        }

        for (Dice dice : dicesToManageAttributes) {
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

