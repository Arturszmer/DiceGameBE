package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.DiceGameBE.assemblers.PlayerAssembler.toEntity;

@Service
@RequiredArgsConstructor
public class GameCreatorServiceImpl implements GameCreatorService{

    private final GameDao gameDaoImpl;

    @Override
    public Game createGame(CreatePlayerDto createPlayerDto) {
        Game game = new Game(toEntity(createPlayerDto));
        return gameDaoImpl.saveGame(game);
    }

    @Override
    public Game findGame(String gameId) {
        return gameDaoImpl.getGame(gameId).orElse(null);
    }
}
