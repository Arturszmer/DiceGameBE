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
import java.util.stream.Collectors;

import static com.example.DiceGameBE.dto.message.MessageMapper.*;
import static com.example.DiceGameBE.utils.MessageContents.*;
import static com.example.DiceGameBE.utils.MessageTypes.*;


//TODO patrząc na ilość Twoich zmian mogę powiedzieć że się pogubiłem, i nie wiem czy te metody liczące do tych zmian pasują. Ale spokojnie dziś ściągne sobie Twoje niedzielne zmiany i bedę rzeźbił dalej

@Service
@RequiredArgsConstructor
public class DiceServiceImpl implements DiceService {

     private final GameRepository repository;
     private final Random random = new Random();

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

    public RollDicesResult rollDicesResult(RollDto rollDto) {
        RollDicesResult result = new RollDicesResult(rollDto.dices());

        Game game = repository
                .findById(rollDto.gameId())
                .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

        if (result.getDices().isEmpty() && game.getDices().isEmpty()) {
            getNumbersFromFirstRoll(result.getDices());
        } else {
            getNumbersFromRoll(result.getDices());
        }
        allPointsFromRoll(result.getDices());
        checkPossibilityToNextRoll(result.getDices(), game);

        setAttributes(result.getDices());

        countMultiples(result.getDices());

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

    public void getNumbersFromRoll(List<Dice> dices) {
        int dicesToRoll = 5 - dices.size();

        addRollForNonImmutable(dices, dicesToRoll);
        addRollForImmutable(dices, dicesToRoll);

        setAttributes(dices);
    }

    private void addRollForNonImmutable(List<Dice> dices, int dicesToRoll) {
        List<Dice> nonImmutableDices = dices.stream()
                .filter(d -> !d.isImmutable())
                .toList();

        for (Dice dice : nonImmutableDices) {
            for (int i = 0; i < dicesToRoll - nonImmutableDices.size(); i++) {
                int value = random.nextInt(6) + 1;
                dice.insertNewValueFromRoll(value);
            }
        }
    }

    private void addRollForImmutable(List<Dice> dices, int dicesToRoll) {
        List<Dice> immutableDices = dices.stream()
                .filter(d -> d.isImmutable() || d.isChecked())
                .toList();

        for (Dice dice : immutableDices) {
            for (int i = 0; i < dicesToRoll; i++) {
                int value = random.nextInt(6) + 1;
                dice.insertNewValueFromRoll(value);
            }
        }
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

    public static int allPointsFromRoll(List<Dice> dices) {
        int allPointsFromRoll = 0;

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

    public static int countMultiples(List<Dice> multiplesArray) {
        if (multiplesArray.get(0).getValue() != 1) {
            return multiplesArray.get(0).getValue() * 10 * (multiplesArray.size() - 2);
        } else {
            return 10 * (multiplesArray.size() - 2) * 10;
        }
    }
 }
