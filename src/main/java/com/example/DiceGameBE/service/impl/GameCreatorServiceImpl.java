package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.ConnectMessage;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GameCreatorService;
import com.example.DiceGameBE.utils.GameValidations;
import com.example.DiceGameBE.common.GameplayContents;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.assemblers.PlayerAssembler.toEntity;
import static com.example.DiceGameBE.common.ErrorContents.*;
import static com.example.DiceGameBE.common.MessageTypes.GAME_CREATED;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameCreatorServiceImpl implements GameCreatorService {

    private final GameRepository gameRepository;

    @Override
    public Game createGame(AdminPlayerDto createAdminPlayerDto) {

        Game game = new Game(toEntity(createAdminPlayerDto));
        Game saved = gameRepository.save(game);
        log.info("The game has been created by CRUD, game id: {}", saved.getGameId());
        return saved;
    }

    @Override
    public GameMessage connectGame(ConnectMessage message) {

        Optional<Game> gameOpt = gameRepository.findById(message.getGameId());
        if(gameOpt.isPresent() && GameValidations.gameStatusValid(gameOpt.get())){
            Game game = gameOpt.get();
            return MessageMapper.gameToMessage(game, GameplayContents.CONNECT.getContent(message.getPlayerName()), GAME_CREATED);
        } else {
            return MessageMapper.errorMessage(GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(message.getGameId()));
        }
    }

    @Override
    public String deleteAllGames() {

        gameRepository.deleteAll();
        return "All games are removed";
    }
}
