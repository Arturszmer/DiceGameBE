package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;

public interface DiceService {

    GameMessage rollDices(DiceMessage message);

    GameMessage checkDices(DiceMessage message);
}
