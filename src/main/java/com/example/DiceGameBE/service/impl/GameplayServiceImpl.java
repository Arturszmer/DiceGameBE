package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GameplayService;
import com.example.DiceGameBE.utils.MessageTypes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.utils.MessageContents.*;

@RequiredArgsConstructor
@Service
public class GameplayServiceImpl implements GameplayService {

    private final GameRepository gameRepository;

    @Override
    public GameMessage nextPlayer(Message message) {
        Optional<Game> gameOpt = gameRepository.findById(message.getGameId());

        if(gameOpt.isEmpty() || gameOpt.get().getGameStatus() == GameStatus.FINISHED){
            return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED);
        }

        Game game = gameOpt.get();

        game.getCurrentPlayer().getValidations().setAllFalse();
        game.nextTurn();
        game.getDices().clear();

        gameRepository.save(game);

        return MessageMapper.gameToMessage(game,
                "Next player is: " + game.getCurrentPlayer().getName(),
                MessageTypes.GAME_TURN_CHANGED);
    }
}
