package com.example.DiceGameBE.serialization;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

public class SerializeGame {

    //TODO: probably it wont be needed - to remove

    public static String serializeGameToJson(Game game) {
        JSONObject gameObject = new JSONObject();
        gameObject.put("gameId", game.getGameId());
        gameObject.put("gameStatus", game.getGameStatus());
        JSONArray playersObject = new JSONArray();
        for(Player player : game.getPlayers()){
            JSONObject playerObject = new JSONObject();
            playerObject.put("playerId", player.getId());
            playerObject.put("playerName", player.getName());
            playerObject.put("points", player.getPoints());
            playerObject.put("validations", player.getValidations());
            playersObject.put(playerObject);
        }
        gameObject.put("players", playersObject);
        gameObject.put("adminPlayer", game.getAdminPlayer());
        gameObject.put("currentTurn", game.getCurrentTurn());
        gameObject.put("startGame", game.getStartGameTime());

        return gameObject.toString();
    }

}
