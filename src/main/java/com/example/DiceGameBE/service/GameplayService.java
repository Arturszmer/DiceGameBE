package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;

public interface GameplayService {

    Game nextPlayer(String gameId);
}
