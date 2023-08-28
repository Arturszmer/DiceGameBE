package com.example.DiceGameBE.service.impl;

import com.example.DiceGameBE.model.Dice;
import java.util.List;

public interface DiceService {


    List<Dice> rollDices(int numberOfDicesToRoll);

    void setAttributes(List<Dice> dices);
}
