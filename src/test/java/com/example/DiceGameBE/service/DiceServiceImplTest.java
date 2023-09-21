package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.RollDicesResult;
import com.example.DiceGameBE.dto.RollDto;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.utils.DiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class DiceServiceImplTest {

    @Spy
    private DiceServiceImpl diceService;
    private final GameRepository gameRepositoryMock = mock(GameRepository.class);
    private final static String GAME_ID = "gameId";

    @BeforeEach
    void setup(){
        diceService = new DiceServiceImpl(gameRepositoryMock);
    }


    @Test
    public void should_return_five_dices_with_correct_attributes() {

        //given
        RollDto rollDto = new RollDto(Collections.emptyList(), GAME_ID);

        //when
        when(gameRepositoryMock.findById(GAME_ID)).thenReturn(Optional.of(new Game()));
        RollDicesResult rollDicesResult = diceService.rollDices(rollDto);

        //then
        assertEquals(5, rollDicesResult.getDices().size());
        for (Dice dice : rollDicesResult.getDices()) {
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
    @MethodSource("dicesToCount")
    public void testCalculatePointsForAllGoodNumber(List<Dice> dices, int result) {

        //when
        int points3 = DiceServiceImpl.countPoints(dices);

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
        int points5 = DiceServiceImpl.allPointsFromRoll(dices5);

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
        int points6 = DiceServiceImpl.temporaryPoints(dices6);

        //then
        assertEquals(20, points6);
    }
}
