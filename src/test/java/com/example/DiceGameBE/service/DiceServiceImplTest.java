package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.DiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DiceServiceImplTest {

    private DiceServiceImpl diceService;

    private final GameRepository gameRepositoryMock = mock(GameRepository.class);
    private final static String GAME_ID = "gameId";

    @BeforeEach
    void setup(){
        diceService = new DiceServiceImpl(gameRepositoryMock);
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
    public void testCalculateAllPointsFromRoll(List<Dice> dices, int result) {

        //when
        int points3 = DiceServiceImpl.allPointsFromRoll(dices);

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
}
