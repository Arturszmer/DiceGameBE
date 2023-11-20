package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.dto.message.SimpMessage;
import com.example.DiceGameBE.dto.message.WinnerMessage;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GameplayService;
import com.example.DiceGameBE.utils.GameValidations;
import com.example.DiceGameBE.common.GameplayContents;
import com.example.DiceGameBE.common.MessageTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.common.ErrorContents.*;

@RequiredArgsConstructor
@Service
public class GameplayServiceImpl implements GameplayService {

    private final GameRepository gameRepository;

    @Override
    public GameMessage nextPlayer(Message message) {
        Optional<Game> gameOpt = gameRepository.findById(message.getGameId());

        if(gameOpt.isPresent() && GameValidations.gameStatusValid(gameOpt.get())){
            Game game = gameOpt.get();

            prepareGameForNextTurn(game);

            gameRepository.save(game);

            return MessageMapper.gameToMessage(game,
                    GameplayContents.NEXT_PLAYER.getContent(game.getCurrentPlayer().getName()),
                    MessageTypes.GAME_TURN_CHANGED);
        } else {
            return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()));
        }
    }

    @Override
    public GameMessage savePoints(SimpMessage message) {
        return gameRepository.findById(message.getGameId()).map(game -> {
            if(GameValidations.gameStatusValid(game)){

                game.getCurrentPlayer().savePoints(game.getPoints().getPoints());
                prepareGameForNextTurn(game);
                gameRepository.save(game);

                return MessageMapper.gameToMessage(game,
                        GameplayContents.NEXT_PLAYER.getContent(game.getCurrentPlayer().getName()),
                        MessageTypes.GAME_TURN_CHANGED);
            } else {
                return MessageMapper.errorMessage(GAME_ERROR_GAME_IS_FINISHED.getContent(message.getGameId()));
            }
        }).orElseGet(() -> MessageMapper.errorMessage(GAME_ERROR_GAME_NOT_EXIST.getContent(message.getGameId())));
    }

    @Override
    public GameMessage winGame(WinnerMessage message) {
        return gameRepository.findById(message.getGameId()).map(game -> {
            if(GameValidations.gameStatusValid(game)){

                game.getCurrentPlayer().setPoints(1000);

                if(message.isReplay()){
                    prepareNewGame(game);
                    gameRepository.save(game);

                    return MessageMapper.gameToMessage(game,
                            GameplayContents.WINNER.getContent(game.getCurrentPlayer().getName()),
                            MessageTypes.GAME_WINNER);
                }
                game.setGameStatus(GameStatus.FINISHED);
                gameRepository.save(game);

                return MessageMapper.gameToMessage(game,
                        GameplayContents.WINNER.getContent(game.getCurrentPlayer().getName()),
                        MessageTypes.GAME_WINNER);
            } else {
                return MessageMapper.errorMessage(GAME_ERROR_GAME_IS_FINISHED.getContent(message.getGameId()));
            }
        }).orElseGet(() -> MessageMapper.errorMessage(GAME_ERROR_GAME_NOT_EXIST.getContent(message.getGameId())));
    }

    private void prepareNewGame(Game game) {
        game.restart();
    }

    private static void prepareGameForNextTurn(Game game) {
        game.getCurrentPlayer().getValidations().setAllFalse();
        game.nextTurn();
        game.getDices().clear();
        game.clearPoints();
    }

}
