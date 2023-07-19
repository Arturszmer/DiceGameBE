package com.example.DiceGameBE.dto;

import lombok.Getter;

@Getter
public class PlayerDto {
    private final Integer id;
    private final String username;
    private final Integer points;

    public PlayerDto(Integer id, String username) {
        this.id = id;
        this.username = username;
        this.points = 0;
    }
}
