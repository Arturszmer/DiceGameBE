package com.example.DiceGameBE.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveMessage implements Message {

    private String type;
    private String gameId;
    private String content;

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
