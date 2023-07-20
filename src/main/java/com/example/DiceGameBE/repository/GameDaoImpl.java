package com.example.DiceGameBE.repository;


import com.example.DiceGameBE.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameDaoImpl implements GameDao{
    private final RedisTemplate redisTemplate;

    public Game saveGame(Game game){
        try {
            String gameKey = "game";
            redisTemplate.opsForHash().put(gameKey, game.getGameId(), game);
            return game;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Game> getGame(String gameId){
        String gameKey = "game";
        var gameFounded = (Game) redisTemplate.opsForHash().get(gameKey, gameId);
        return Optional.ofNullable(gameFounded);
    }

}
