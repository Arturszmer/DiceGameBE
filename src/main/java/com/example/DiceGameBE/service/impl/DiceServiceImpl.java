 package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.model.Points;
import com.example.DiceGameBE.model.Validations;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.DiceService;
import com.example.DiceGameBE.utils.GameValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

import static com.example.DiceGameBE.common.ErrorContents.GAME_ERROR_BAD_OWNER;
import static com.example.DiceGameBE.common.ErrorContents.GAME_ERROR_NOT_FOUND_OR_FINISHED;
import static com.example.DiceGameBE.common.MessageTypes.*;
import static com.example.DiceGameBE.dto.message.MessageMapper.*;
import static com.example.DiceGameBE.utils.DicesCalculator.count;
import static com.example.DiceGameBE.utils.DicesCalculator.countPointsFromRoll;

 @Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {

     private final GameRepository repository;
     private final Random random = new Random();

     @Override
     public GameMessage rollDices(DiceMessage message, String owner) {

         Optional<Game> gameOpt = repository.findById(message.getGameId());

         if(gameOpt.isPresent() && GameValidations.gameStatusValid(gameOpt.get())){
             Game game = gameOpt.get();

             if(GameValidations.gameOwnerInvalid(game, owner)){
                 return MessageMapper.errorMessage(GAME_ERROR_BAD_OWNER.getContent(owner));
             }

             List<Dice> dices = message.getDices();

             roll(dices, game);
             game.setDices(dices);

             repository.save(game);

             return gameToMessage(game, GAME_ROLL);
         } else {
             return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()));
         }
     }


     @Override
     public GameMessage checkDices(DiceMessage message, String owner) {
         Optional<Game> gameOpt = repository.findById(message.getGameId());
         if(gameOpt.isPresent() && GameValidations.gameStatusValid(gameOpt.get())){

             Game game = gameOpt.get();

             if(GameValidations.gameOwnerInvalid(game, owner)){
                 return MessageMapper.errorMessage(GAME_ERROR_BAD_OWNER.getContent(owner));
             }

             List<Dice> dices = message.getDices();


             game.setDices(dices);
             Points points = game.getPoints();
             points.setPoints(count(dices, points.getTemporaryPoints()));

             checkPossibilityToNextRoll(dices, game);

             repository.save(game);

             GameMessage gameMessage = gameToMessage(game);
             gameMessage.setType(GAME_CHECK.getType());

             return gameMessage;
         } else {
             return GameMessage.builder()
                     .type(ERROR.getType())
                     .content(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()))
                     .build();
         }
     }

    private void roll(List<Dice> dices, Game game) {
        if(dices.isEmpty()){
            rollingAllDices(dices);
            checkPossibilityToNextRoll(dices, game);
        } else {
            rollingPartOfTheDices(dices, game.getPoints());
            checkPossibilityToNextRoll(dices, game);
        }
        checkWinnerStatus(dices, game);
    }

     private static void checkWinnerStatus(List<Dice> dices, Game game) {
         Validations validations = game.getCurrentPlayer().getValidations();
         int pointsFromRoll = countPointsFromRoll(dices, game.getPoints().getTemporaryPoints());

         if(pointsFromRoll + game.getCurrentPlayer().getPoints() == 1000){
             validations.setAllFalse();
             validations.setWinner(true);

         }
         if(pointsFromRoll + game.getCurrentPlayer().getPoints() > 1000){
             validations.setAllFalse();
             validations.setNextPlayer(true);
         }
     }

     private void checkPossibilityToNextRoll(List<Dice> dices, Game game) {

         Validations validations = game.getCurrentPlayer().getValidations();
         boolean canRoll = !dices.stream().filter(dice -> dice.isGoodNumber() && !dice.isImmutable()).toList().isEmpty();
         boolean beChecked = !dices.stream().filter(dice -> dice.isChecked() && !dice.isImmutable()).toList().isEmpty();

         validations.setNextPlayer(!canRoll);
         itCanBeSaved(game.getPoints(), game.getCurrentPlayer(), canRoll && beChecked);
     }

    private void itCanBeSaved(Points points, Player currentPlayer, boolean canRoll) {
        Validations playerValidations = currentPlayer.getValidations();
        if(!playerValidations.isNextPlayer()){
            playerValidations.setRolling(canRoll);
            int totalPoints = points.getPoints();
            playerValidations.setSaved(currentPlayer.getPoints() < 100
                    ? totalPoints >= 100
                    : totalPoints >= 25);
        } else {
            playerValidations.setSaved(false);
            playerValidations.setRolling(canRoll);
        }
    }

     private void rollingAllDices(List<Dice> dices) {
            for (int i = 0; i < 5; i++) {
                int value = random.nextInt(6) +1;
                dices.add(new Dice(value));
            }
            setAttributes(dices);
    }

     private void rollingPartOfTheDices(List<Dice> dices, Points points) {
         if(dices.stream().filter(dice -> dice.isChecked() || dice.isImmutable()).toList().size() == 5){
             for (int i = 0; i < 5; i++) {
                 int value = random.nextInt(6) +1;
                 dices.get(i).insertNewValueFromRoll(value);
             }
             points.managePointsBeforeNextRollByAllDices();
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

