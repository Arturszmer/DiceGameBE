package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiceServiceTest {

    private DiceService diceService;

    @BeforeEach
    void setup() {
        diceService = new DiceService();
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void testRollDice(int numberOfDicesToRoll) {

        //given
        List<Dice> dices = diceService.rollDices(numberOfDicesToRoll);

        //then
        assertEquals(numberOfDicesToRoll, dices.size());
        for (Dice dice : dices) {
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

    @Test
    public void testCalculatePointsForMultipleOne() {
        //given
        List<Dice> dices2 = new ArrayList<>();
        dices2.add(new Dice(1, true, false, true, false));
        dices2.add(new Dice(1, true, false, true, false));
        dices2.add(new Dice(1, true, false, true, false));
        dices2.add(new Dice(1, true, false, true, false));
        dices2.add(new Dice(1, true, false, true, false));
        int points2 = diceService.calculatePointsForMultiple(dices2);

        //then
        assertEquals(400, points2);
    }

    @Test
    public void testCalculatePointsForMultipleTwo() {

        //given
        List<Dice> dices3 = new ArrayList<>();
        dices3.add(new Dice(2, true, false, true, false));
        dices3.add(new Dice(2, true, false, true, false));
        dices3.add(new Dice(2, true, false, true, false));
        dices3.add(new Dice(2, true, false, true, false));
        dices3.add(new Dice(2, true, false, true, false));
        int points3 = diceService.calculatePointsForMultiple(dices3);

        //then
        assertEquals(80, points3);
    }
    @Test
    public void testCalculatePointsForMultipleFive() {

        //given
        List<Dice> dices3 = new ArrayList<>();
        dices3.add(new Dice(5, true, false, true, false));
        int points3 = diceService.calculatePointsForMultiple(dices3);

        //then
        assertEquals(5, points3);
    }

}
