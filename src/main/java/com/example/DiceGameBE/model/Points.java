package com.example.DiceGameBE.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Setter
@Getter
@RedisHash
@NoArgsConstructor
public class Points {

    private int points = 0;
    private int temporaryPoints = 0;
    private int pointsFromRoll = 0;

    public void clear() {
        this.points = 0;
        this.temporaryPoints = 0;
        this.pointsFromRoll = 0;
    }

    public void managePointsBeforeNextRollByAllDices() {
        this.temporaryPoints = points;
    }
}
