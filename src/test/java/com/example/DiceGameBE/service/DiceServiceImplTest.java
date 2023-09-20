package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.impl.DiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

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

        RollDto rollDto = new RollDto(numberOfDicesToRoll, "", true);


        //given
        List<Dice> dices = diceService.rollDices(rollDto);

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
        GameException exception = assertThrows(GameException.class, () -> diceService.rollDices(new RollDto(numberOfDicesToRoll,"", true)));
        assertEquals(BAD_AMOUNT_OF_DICES.getMessage(), exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("dicesToCount")
    public void testCalculatePointsForAllGoodNumber(List<Dice> dices, int result) {

        //when
        int points3 = diceService.countPoints(dices);

        //then
        assertEquals(result, points3);
    }

    private static Stream<Arguments> dicesToCount(){
        return Stream.of(
                Arguments.of(dicesWith20Points(),20),
                Arguments.of(dicesWith30Points(),30),
                Arguments.of(dicesWith50Points(),50)
        );
    }

    private static List<Dice> dicesWith20Points() {
        return List.of(
                DiceBuilder.aDiceBuilder().withValue(1).withGoodNumber().withChecked().withImmutable().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(1).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(2).build()
        );
    }

    private static List<Dice> dicesWith30Points(){
        return List.of(
                DiceBuilder.aDiceBuilder().withValue(1).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(1).withGoodNumber().withChecked().build(),
                DiceBuilder.aDiceBuilder().withValue(2).build()
        );
    }

    private static List<Dice> dicesWith50Points() {
        return List.of(
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().withMultiple().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().withMultiple().build(),
                DiceBuilder.aDiceBuilder().withValue(5).withGoodNumber().withChecked().withMultiple().build(),
                DiceBuilder.aDiceBuilder().withValue(1).withGoodNumber().withChecked().withImmutable().build(),
                DiceBuilder.aDiceBuilder().withValue(2).build()
        );
    }

    @Test
    public void testShouldAllPointsFromRoll() {

        //given
        List<Dice> dices5 = new ArrayList<>();
        dices5.add(new Dice(1, false, true, false, false));
        dices5.add(new Dice(5, false, true, false, false));
        dices5.add(new Dice(2, true, false, true, false));
        dices5.add(new Dice(2, true, false, true, false));
        dices5.add(new Dice(2, true, false, true, false));
        int points5 = diceService.allPointsFromRoll(dices5);

        //then
        assertEquals(35, points5);
    }

    @Test
    public void testShouldTemporaryPoints() {

        //given
        List<Dice> dices6 = new ArrayList<>();
        dices6.add(new Dice(1, false, false, false, false));
        dices6.add(new Dice(5, false, false, false, false));
        dices6.add(new Dice(2, true, true, true, false));
        dices6.add(new Dice(2, true, true, true, false));
        dices6.add(new Dice(2, true, true, true, false));
        int points6 = diceService.temporaryPoints(dices6);

        //then
        assertEquals(20, points6);
    }
}
