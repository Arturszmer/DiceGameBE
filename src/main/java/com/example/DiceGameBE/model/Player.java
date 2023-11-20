package com.example.DiceGameBE.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash
@NoArgsConstructor
public class Player implements Serializable {
    private Integer id;
    private String name;
    private Integer points;
    private boolean active;
    private Validations validations;

    public Player(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.points = 0;
        this.active = true;
        this.validations = new Validations(
                false,
                id == 0, false, false);
    }

    public void savePoints(int points){
        this.points += points;
    }

    public void restart() {
        this.points = 0;
        this.validations.setAllFalse();
    }
}
