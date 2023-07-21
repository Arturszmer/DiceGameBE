package com.example.DiceGameBE.repository;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {

    List<Game> findGamesByGameStatus(GameStatus gameStatus);
}
