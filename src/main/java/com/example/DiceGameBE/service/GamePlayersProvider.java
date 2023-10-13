package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.SimpMessage;
import com.example.DiceGameBE.model.Game;

public interface GamePlayersProvider {

    Game joinToOpenGame(NewPlayerDto playerName, String gameId);
    GameMessage leaveGame(SimpMessage message, String playerName);
}
