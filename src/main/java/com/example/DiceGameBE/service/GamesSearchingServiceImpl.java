package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GamesSearchingServiceImpl implements GamesSearchingService{

    private final GameRepository gameRepository;

    @Override
    public Game findGameByGameId(String gameId) {
        return gameRepository.findById(gameId).orElse(null);
    }

    @Override
    public List<Game> findOpenGames(GameStatus status) {
        return gameRepository.findGamesByGameStatus(status);
    }
}