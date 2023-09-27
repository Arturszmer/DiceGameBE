package com.example.DiceGameBE.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash
@NoArgsConstructor
@AllArgsConstructor
public class Validations implements Serializable {

    @JsonProperty(value = "isNextPlayer")
    private boolean isNextPlayer;
    @JsonProperty(value = "isRolling")
    private boolean isRolling;
    @JsonProperty(value = "isSaved")
    private boolean isSaved;
    @JsonProperty(value = "isWinner")
    private boolean isWinner;

    public void setAllFalse() {
        isNextPlayer = false;
        isRolling = false;
        isSaved = false;
        isWinner = false;
    }
}
