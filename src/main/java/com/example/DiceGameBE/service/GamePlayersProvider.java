package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;

public interface GamePlayersProvider {

    boolean addPlayerToOpenGame(CreatePlayerDto playerDto, String gameId);
}
