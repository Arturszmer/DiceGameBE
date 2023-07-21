package com.example.DiceGameBE.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash
@NoArgsConstructor
@AllArgsConstructor
public class Validations implements Serializable {
    private boolean isNextPlayer;
    private boolean isRolling;
    private boolean isSaved;
    private boolean isWinner;
}
