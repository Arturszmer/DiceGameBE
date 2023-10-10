package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.dto.message.*;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GamePlayersProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.utils.MessageContents.GAME_ERROR_NOT_FOUND_OR_FINISHED;
import static com.example.DiceGameBE.utils.MessageTypes.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository repository;

    @Override
    public Game joinToOpenGame(NewPlayerDto newPlayer, String gameId) {

        Game game = repository.findById(gameId).orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));

        game.addPlayer(newPlayer.playerName());
        log.info("New Player to game id: {} has been added, his name is: {}", game.getGameId(), newPlayer.playerName());
        repository.save(game);

        return game;
    }

    @Override
    public GameMessage leaveGame(LeaveMessage message, String playerName) {

        Optional<Game> gameOpt = repository.findById(message.getGameId());
        if(gameOpt.isEmpty() || gameOpt.get().getGameStatus() == GameStatus.FINISHED){
            return GameMessage.builder()
                    .type(ERROR.name())
                    .content(GAME_ERROR_NOT_FOUND_OR_FINISHED.name())
                    .build();
        }

        Game game = gameOpt.get();
        game.removePlayerByName(playerName);

        repository.save(game);

        return MessageMapper.gameToMessage(game,
                String.format("Player %s is disconnected this game", playerName),
                GAME_LEAVE);
    }
}