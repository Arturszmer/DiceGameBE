package com.example.DiceGameBE.service;

public interface GamePlayersProvider {

    boolean addPlayerToOpenGame(String playerName, String gameId);
}
