package com.example.DiceGameBE.utils;

import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.service.DiceModels;
import com.example.DiceGameBE.service.UtilsTests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DicesCalculatorTest {

    @ParameterizedTest
    @MethodSource("prepareRolls")
    public void should_count_points_from_roll(List<Dice> dicesFromRoll, int points) {
        // given
        List<Dice> dices = UtilsTests.setDicesAttributes(dicesFromRoll);
        List<Dice> checkedDices = UtilsTests.setCheckedAllDices(dices);

        // when
        int pointsFromRoll = DicesCalculator.count(checkedDices);

        // then
        assertEquals(points, pointsFromRoll);
    }

    private static Stream<Arguments> prepareRolls(){
        return Stream.of(
                Arguments.of(DiceModels.allFalseDices(1, 2, 3, 3, 6), 10),
                Arguments.of(DiceModels.allFalseDices(5, 2, 3, 3, 6), 5),
                Arguments.of(DiceModels.allFalseDices(5, 1, 3, 3, 6), 15),
                Arguments.of(DiceModels.allFalseDices(1, 1, 1, 3, 6), 100),
                Arguments.of(DiceModels.allFalseDices(3, 3, 1, 3, 6), 40),
                Arguments.of(DiceModels.allFalseDices(5, 5, 5, 5, 6), 100),
                Arguments.of(DiceModels.allFalseDices(6, 6, 6, 6, 6), 180),
                Arguments.of(DiceModels.allFalseDices(5, 1, 2, 2, 2), 35)
        );
    }
}