package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.dto.message.*;
import com.example.DiceGameBE.exceptions.GameErrorResult;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GamePlayersProvider;
import com.example.DiceGameBE.common.GameplayContents;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.common.ErrorContents.*;
import static com.example.DiceGameBE.common.MessageTypes.*;

@Slf4j
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository repository;
    private final String PATH_URL;

    public GamePlayersProviderImpl(GameRepository repository,
                                   @Value("${url.basicPath}") String PATH_URL) {
        this.repository = repository;
        this.PATH_URL = PATH_URL;
    }

    @Override
    public Game joinToOpenGame(NewPlayerDto newPlayer, String gameId) {

        Game game = repository.findById(gameId)
                .orElseGet(() -> repository.findGamesByInvitationToken(gameId).stream().findFirst()
                        .orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX)));

        game.addPlayer(newPlayer.playerName());
        log.info("New Player to game id: {} has been added, his name is: {}", game.getGameId(), newPlayer.playerName());
        repository.save(game);

        return game;
    }

    @Override
    public GameMessage leaveGame(SimpMessage message, String playerName) {

        Optional<Game> gameOpt = repository.findById(message.getGameId());
        if(gameOpt.isPresent()){
            Game game = gameOpt.get();
            boolean isAllPlayersInactive = game.inactivePlayerByName(playerName);

            if(isAllPlayersInactive){
                game.setGameStatus(GameStatus.FINISHED);
                repository.save(game);

                return MessageMapper.gameToMessage(game,
                        GameplayContents.DISCONNECT_AND_CLOSE.getContent(playerName),
                        GAME_LEAVE);
            } else {
                repository.save(game);

                return MessageMapper.gameToMessage(game,
                        GameplayContents.DISCONNECT.getContent(playerName),
                        GAME_LEAVE);
            }
        } else {
            return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()));
        }
    }

    @Override
    public String generateLink(String gameId) {
        Game game = repository.findById(gameId).orElseThrow(() -> new GameException(GameErrorResult.GAME_NOT_FOUND_EX));
        String token = game.generateToken();
        repository.save(game);
        return PATH_URL + token;
    }
}