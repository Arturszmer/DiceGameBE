package com.example.DiceGameBE.repository;


import com.example.DiceGameBE.model.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import static com.example.DiceGameBE.serialization.SerializeGame.serializeGameToJson;

@Repository
public class GameRepository {
    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    public GameRepository() {
        this.objectMapper = new ObjectMapper();
        this.jedis = new Jedis("localhost", 6379);
    }

    public String saveGame(Game game){
        String gameKey = "game:" + game.getGameId();
        String gameJson = serializeGameToJson(game);
        jedis.set(gameKey, gameJson);
        return jedis.get(gameKey);
    }

    public Game getGame(String gameId){
        String gameKey = "game:" + gameId;
        String gameJson = jedis.get(gameKey);
        return objectMapper.convertValue(gameJson, Game.class);
    }

}
