package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.JoinMessage;
import com.example.DiceGameBE.dto.message.MessageMapper;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GameCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.assemblers.PlayerAssembler.toEntity;
import static com.example.DiceGameBE.utils.MessageTypes.GAME_CREATED;

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
    public GameMessage connectGame(JoinMessage message) {

        Optional<Game> gameOpt = gameRepository.findById(message.getGameId());
        if(gameOpt.isEmpty() || gameOpt.get().getGameStatus() == GameStatus.FINISHED){
            return MessageMapper.errorMessage();
        }

        Game game = gameOpt.get();

        return MessageMapper.gameToMessage(game, GAME_CREATED);
    }

    @Override
    public String deleteAllGames() {

        gameRepository.deleteAll();
        return "All games are removed";
    }
}
