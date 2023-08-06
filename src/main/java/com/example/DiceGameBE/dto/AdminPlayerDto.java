package com.example.DiceGameBE.dto;

import lombok.Getter;

@Getter
public class AdminPlayerDto {
    private final Integer id;
    private final String name;
    private final Integer points;

    public AdminPlayerDto(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.points = 0;
    }
}
