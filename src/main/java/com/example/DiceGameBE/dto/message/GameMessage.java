package com.example.DiceGameBE.dto.message;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage implements Message {

    private String type;
    private String gameId;
    private String content;
    private Game game;
    private Player currentPlayer;

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getGameId() {
        return gameId;
    }

    @Override
    public String getContent() {
        return content;
    }
}
