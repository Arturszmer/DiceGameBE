package com.example.DiceGameBE.dto.message;

import com.example.DiceGameBE.model.Dice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiceMessage implements Message{

    private String type;
    private String gameId;
    private String content;
    private List<Dice> dices;

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
