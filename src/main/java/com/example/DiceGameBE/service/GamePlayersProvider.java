package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;

public interface GamePlayersProvider {

    void addPlayerToOpenGame(CreatePlayerDto playerDto);
}
