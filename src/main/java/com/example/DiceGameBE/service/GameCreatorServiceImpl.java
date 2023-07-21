package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.DiceGameBE.assemblers.PlayerAssembler.toEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameCreatorServiceImpl implements GameCreatorService{

    private final GameRepository gameRepository;

    @Override
    public Game createGame(CreatePlayerDto createPlayerDto) {
        Game game = new Game(toEntity(createPlayerDto));
        Game saved = gameRepository.save(game);
        log.info("The game has been created by CRUD, game id: {}", saved.getGameId());
        return saved;
    }

    @Override
    public String deleteAllGames() {
        gameRepository.deleteAll();
        return "All games are removed";
    }
}
