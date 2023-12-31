package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GamesSearchingService {
    Game findGameByGameId(String gameId);

    List<Game> findOpenGames(GameStatus status);

    Page<Game> findGamesByPage(int page, int size);
}
