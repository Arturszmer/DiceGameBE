package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;

public interface GameplayService {

    GameMessage nextPlayer(Message gameId);
}
