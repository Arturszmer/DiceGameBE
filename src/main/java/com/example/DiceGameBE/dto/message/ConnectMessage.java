package com.example.DiceGameBE.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectMessage implements Message{

    private String type;
    private String gameId;
    private String content;
    private String playerName;

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
