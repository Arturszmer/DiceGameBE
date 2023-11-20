package com.example.DiceGameBE.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WinnerMessage implements Message {

    private String type;
    private String gameId;
    private String content;

    @JsonProperty(value = "isReplay")
    private boolean isReplay = false;

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public String getGameId() {
        return this.gameId;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
