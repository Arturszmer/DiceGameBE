package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Game;
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

            game.getCurrentPlayer().getValidations().setAllFalse();
            game.nextTurn();
            game.getDices().clear();

            gameRepository.save(game);

            return MessageMapper.gameToMessage(game,
                    GameplayContents.NEXT_PLAYER.getContent(game.getCurrentPlayer().getName()),
                    MessageTypes.GAME_TURN_CHANGED);
        } else {
            return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()));
        }
    }
}
