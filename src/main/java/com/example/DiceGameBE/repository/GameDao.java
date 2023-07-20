package com.example.DiceGameBE.repository;

import com.example.DiceGameBE.model.Game;

import java.util.Optional;

public interface GameDao {
    Game saveGame(Game game);

    Optional<Game> getGame(String gameId);
}
