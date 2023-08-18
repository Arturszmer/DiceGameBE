package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiceServiceTest {

    private DiceService diceService;

    @BeforeEach
    void setup (){
        diceService = new DiceService();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testRollDice (int numberOfDicesToRoll){

        //given
        List<Dice> diceList = diceService.rollDices(numberOfDicesToRoll);

        //then
        assertEquals(numberOfDicesToRoll, diceList.size());
        for (Dice dice : diceList) {
            assertTrue(dice.getValue() >= 1 && dice.getValue() <= 6);
            switch (dice.getValue()) {
                case 1, 5 -> assertTrue(dice.isGoodNumber());
                case 2, 3, 4, 6 -> {
                    if (dice.isMultiple()) {
                        assertTrue(dice.isGoodNumber());
                    } else {
                        assertFalse(dice.isGoodNumber());
                    }
                }
            }
        }
    }
}
