package com.example.DiceGameBE.dto.message;

import com.example.DiceGameBE.model.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlayerMessage implements Message{

    private String type;
    private String gameId;
    private String content;
    @Getter
    private Player player;
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
