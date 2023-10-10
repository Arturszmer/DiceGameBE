package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;

public interface DiceService {

    RollDicesResult rollDices(RollDto rollDicesResult);
    GameMessage rollDices(DiceMessage message);

    RollDicesResult checkDices(RollDto rollDto);
    GameMessage checkDices(DiceMessage message);
}
