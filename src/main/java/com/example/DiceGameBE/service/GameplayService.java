package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;
import com.example.DiceGameBE.dto.message.SimpMessage;
import com.example.DiceGameBE.dto.message.WinnerMessage;

public interface GameplayService {

    GameMessage nextPlayer(Message gameId);

    GameMessage savePoints(SimpMessage message);

    GameMessage winGame(WinnerMessage message);
}
