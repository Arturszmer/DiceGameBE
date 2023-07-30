package com.example.DiceGameBE.dto;

import lombok.Getter;

@Getter
public class AdminPlayerDto {
    private final Integer id;
    private final String username;
    private final Integer points;

    public AdminPlayerDto(Integer id, String username) {
        this.id = id;
        this.username = username;
        this.points = 0;
    }
}
