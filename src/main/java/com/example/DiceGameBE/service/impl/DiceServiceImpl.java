package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Validations;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.DiceService;
import com.example.DiceGameBE.utils.GameValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.DiceGameBE.common.ErrorContents.GAME_ERROR_BAD_OWNER;
import static com.example.DiceGameBE.common.ErrorContents.GAME_ERROR_NOT_FOUND_OR_FINISHED;
import static com.example.DiceGameBE.common.MessageTypes.*;
import static com.example.DiceGameBE.dto.message.MessageMapper.*;

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

            manageDicesFromRoll(dices, game);
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

            checkPossibilityToNextRoll(dices, game);

            game.setDices(dices);
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

    private void manageDicesFromRoll(List<Dice> dices, Game game) {
        if(dices.isEmpty()){
            rollingAllDices(dices);
            checkPossibilityToNextRoll(dices, game);
        } else {
            rollingPartOfTheDices(dices);
            checkPossibilityToNextRoll(dices, game);
        }
    }

    private void checkPossibilityToNextRoll(List<Dice> dices, Game game) {

        Validations validations = game.getCurrentPlayer().getValidations();
        boolean canRoll = !dices.stream().filter(dice -> dice.isGoodNumber() && !dice.isImmutable()).toList().isEmpty();
        validations.setRolling(canRoll);
        validations.setNextPlayer(!canRoll);
    }

    private void rollingAllDices(List<Dice> dices) {
        for (int i = 0; i < 5; i++) {
            int value = random.nextInt(6) +1;
            dices.add(new Dice(value));
        }

        setAttributes(dices);
    }

    public void rollingPartOfTheDices(List<Dice> dices) {
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
                .filter(d -> !d.isMultiple() && d.isChecked() && !d.isImmutable())
                .toList();

        for (Dice dice : restDices) {
            if (dice.getValue() == 1) {
                allPointsFromRoll += 10;
            } else {
                allPointsFromRoll += 5;
            }
        }

        return allPointsFromRoll;
    }

    private static int countMultiples(List<Dice> multiplesArray) {
        if (multiplesArray.get(0).getValue() != 1) {
            return multiplesArray.get(0).getValue() * 10 * (multiplesArray.size() - 2);
        } else {
            return 10 * (multiplesArray.size() - 2) * 10;
        }
    }
}
