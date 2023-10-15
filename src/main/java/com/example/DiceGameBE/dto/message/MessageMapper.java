package com.example.DiceGameBE.dto.message;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.utils.MessageTypes;

import static com.example.DiceGameBE.utils.MessageTypes.ERROR;

public class MessageMapper {

    public static GameMessage gameToMessage(Game game) {

        return GameMessage.builder()
                .gameId(game.getGameId())
                .game(game)
                .currentPlayer(game.getCurrentPlayer())
                .build();
    }

    public static GameMessage gameToMessage(Game game, MessageTypes type) {

        return GameMessage.builder()
                .gameId(game.getGameId())
                .game(game)
                .currentPlayer(game.getCurrentPlayer())
                .type(type.getType())
                .build();
    }

    public static GameMessage gameToMessage(Game game, String content, MessageTypes type) {

        return GameMessage.builder()
                .gameId(game.getGameId())
                .game(game)
                .currentPlayer(game.getCurrentPlayer())
                .content(content)
                .type(type.getType())
                .build();
    }

    public static GameMessage errorMessage(String content){
        return GameMessage.builder()
                .type(ERROR.getType())
                .content(content)
                .build();
    }
}
