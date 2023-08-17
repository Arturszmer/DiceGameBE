package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceServiceTest {

    private DiceService diceService;

    @BeforeEach
    void setup (){diceService = new DiceService();}

    @Test
    public void testRollDice (){

        //given
        List<Dice> diceList = diceService.rollDices();

        //then
        for (Dice dice : diceList) {
            assertTrue(dice.getValue() >= 1 && dice.getValue() <= 6);
        }
    }
}
