package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GameplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GameplayServiceImpl implements GameplayService {

    private final GameRepository gameRepository;

    @Override
    public Game nextPlayer(String gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

        game.getCurrentPlayer().getValidations().setAllFalse();
        game.nextTurn();
        game.getDices().clear();

        return gameRepository.save(game);
    }
}
