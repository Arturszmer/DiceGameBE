package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Player;

public interface GamePlayersProvider {

    Player addPlayerToOpenGame(String playerName, String gameId);
}
