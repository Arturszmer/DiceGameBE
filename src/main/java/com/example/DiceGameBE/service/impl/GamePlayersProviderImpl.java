package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GamePlayersProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository gameRepository;

    @Override
    public Game addPlayerToOpenGame(NewPlayerDto newPlayer, String gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

        game.addPlayer(newPlayer.playerName());
        log.info("New Player to game id: {} has been added, his name is: {}", game.getGameId(), newPlayer.playerName());
        gameRepository.save(game);

        return game;
    }
}
