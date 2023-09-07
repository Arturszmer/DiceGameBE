package com.example.DiceGameBE.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class GameDices implements Serializable {

    private Integer allPointsFromRoll;
    private Integer temporaryPoints;
    private List<Dice> dices = new ArrayList<>();
}
