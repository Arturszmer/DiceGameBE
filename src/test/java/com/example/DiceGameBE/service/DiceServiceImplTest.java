package com.example.DiceGameBE.service;

import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.example.DiceGameBE.exceptions.GameErrorResult.BAD_AMOUNT_OF_DICES;
import static org.junit.jupiter.api.Assertions.*;


class DiceServiceImplTest {

    @Spy
    private DiceServiceImpl diceService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
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
    public void should_add_correct_attributes() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        List<Dice> dices = List.of(
                new Dice(2), new Dice(2), new Dice(2), new Dice(5), new Dice(6)
        );

        Method setAttributesMethod = DiceServiceImpl.class.getDeclaredMethod("setAttributes", List.class);
        setAttributesMethod.setAccessible(true);

        // when
        setAttributesMethod.invoke(diceService, dices);

        // then
        assertTrue(dices.get(0).isMultiple() && dices.get(0).isGoodNumber());
        assertTrue(!dices.get(3).isMultiple() && dices.get(3).isGoodNumber());
        assertFalse(dices.get(4).isMultiple() && dices.get(4).isGoodNumber());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,6,7,8})
    public void testValidateDicesToRoll(int numberOfDicesToRoll) {

            // then
        GameException exception = assertThrows(GameException.class, () -> diceService.rollDices(numberOfDicesToRoll));
        assertEquals(BAD_AMOUNT_OF_DICES.getMessage(), exception.getMessage());
    }
    @Test
    public void testShouldTemporaryPoints() {

        //given
        List<Dice> dices3 = new ArrayList<>();
        dices3.add(new Dice(5, true, true, true, false));
        dices3.add(new Dice(1, true, false, true, false));
        int points3 = diceService.getTemporaryPoints(dices3);

        //then
        assertEquals(5, points3);
    }
    @Test
    public void testShouldAllPointsFromRoll() {

        //given
        List<Dice> dices3 = new ArrayList<>();
        dices3.add(new Dice(5, true, false, true, false));
        dices3.add(new Dice(1, true, false, true, false));
        int points3 = diceService.getAllPointsFromRoll(dices3);

        //then
        assertEquals(6, points3);
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
