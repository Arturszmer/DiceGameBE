package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.model.Dice;
import java.util.List;

public interface DiceService {

    List<Dice> rollDices(RollDto rollDto);
}
