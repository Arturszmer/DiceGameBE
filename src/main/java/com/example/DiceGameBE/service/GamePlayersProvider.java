package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.model.Game;

public interface GamePlayersProvider {

    Game addPlayerToOpenGame(NewPlayerDto playerName, String gameId);
}
