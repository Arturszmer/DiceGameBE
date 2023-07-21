package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;

public class models {

    static CreatePlayerDto createAdminPlayer() {
        return new CreatePlayerDto(0, "admin");
    }
    static CreatePlayerDto createSimplePlayer(int id, String name) {
        return new CreatePlayerDto(id, name);
    }

}
