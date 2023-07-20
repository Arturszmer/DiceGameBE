package com.example.DiceGameBE.repository;


import com.example.DiceGameBE.model.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Optional;

import static com.example.DiceGameBE.serialization.SerializeGame.serializeGameToJson;

@Repository
public class GameDaoImpl implements GameDao{
    private final RedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public GameDaoImpl() {
        this.redisTemplate = new RedisTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public Game saveGame(Game game){
        try {
            String gameKey = "game";
//            String gameJson = objectMapper.writeValueAsString(game);
            redisTemplate.opsForHash().put(gameKey, game.getGameId(), game);
            return game;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Optional<Game> getGame(String gameId){
        String gameKey = "game:" + gameId;
//        String gameJson = jedis.get(gameKey);
        return null;
    }

}
