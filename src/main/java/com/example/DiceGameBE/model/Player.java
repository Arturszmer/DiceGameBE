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
    private Validations validations;

    public Player(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.points = 0;
        this.validations = new Validations(
                false,
                id == 0, false, false);
    }
}
