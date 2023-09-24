package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;

public interface DiceService {

    RollDicesResult rollDices(RollDto rollDicesResult);

    RollDicesResult checkDices(RollDto rollDto);
}
