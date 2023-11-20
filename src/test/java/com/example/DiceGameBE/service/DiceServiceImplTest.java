package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.model.*;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.DiceServiceImpl;
import com.example.DiceGameBE.common.ErrorContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.DiceGameBE.model.GameStatus.FINISHED;
import static com.example.DiceGameBE.model.GameStatus.OPEN;
import static com.example.DiceGameBE.service.DiceBuilder.*;
import static com.example.DiceGameBE.common.MessageTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DiceServiceImplTest {

    private DiceServiceImpl diceService;

    private final GameRepository gameRepositoryMock = mock(GameRepository.class);
    private final static String GAME_ID = "gameId";
    private final static String GAME_OWNER = "PLAYER1";

    @BeforeEach
    void setup(){
        diceService = new DiceServiceImpl(gameRepositoryMock);
    }

    @Test
    public void should_return_five_dices_with_correct_attributes() {

        //given
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, new ArrayList<>());
        prepareSimpleGame(OPEN);

        //when
        GameMessage gameMessage = diceService.rollDices(diceMessage, GAME_OWNER);

        //then
        assertEquals(5, gameMessage.getGame().getDices().size());
        for (Dice dice : gameMessage.getGame().getDices()) {
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
    public void should_return_message_with_error_when_player_who_roll_is_not_current_player() {

        // given
        String currentPlayerName = "PLAYER2";
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, new ArrayList<>());
        Game game = GameBuilder.aGameBuilder()
                .withPlayers(List.of(new Player(0, GAME_OWNER), new Player(1, currentPlayerName)))
                .build();
        game.nextTurn();
        when(gameRepositoryMock.findById(any())).thenReturn(Optional.of(game));

        // when
        GameMessage gameMessage = diceService.rollDices(diceMessage, GAME_OWNER);

        // then
        assertEquals(ErrorContents.GAME_ERROR_BAD_OWNER.getContent(GAME_OWNER), gameMessage.getContent());
    }

    @Test
    public void should_return_error_message_when_game_is_finished() {

        // given
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, new ArrayList<>());
        prepareSimpleGame(FINISHED);

        // when
        GameMessage gameMessage = diceService.rollDices(diceMessage, GAME_OWNER);

        // then
        assertEquals(ErrorContents.GAME_ERROR_NOT_FOUND_OR_FINISHED.getContent(diceMessage.getGameId()), gameMessage.getContent());

    }

    @Test
    public void should_roll_part_of_dices_and_not_change_kept_dices() {

        // given
        List<Dice> dices = getDices(DiceModels.allFalseDices(2, 2, 3));
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, dices);
        prepareSimpleGame(OPEN);

        // when
        GameMessage gameMessage = diceService.rollDices(diceMessage, GAME_OWNER);

        // then
        List<Dice> dicesAfterRoll = gameMessage.getGame().getDices();
        assertEquals(dices.get(0).getValue(), dicesAfterRoll.get(0).getValue());
        assertTrue(dices.get(0).isImmutable());
        assertEquals(dices.get(1).getValue(), dicesAfterRoll.get(1).getValue());
        assertTrue(dices.get(1).isImmutable());

    }

    @Test
    public void should_roll_all_dices_when_all_values_is_good_number_and_checked_or_immutable() {

        // given
        List<Dice> dices = getDices(getMultipleDicesByValue());
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, dices);
        prepareSimpleGame(OPEN);

        // when
        GameMessage gameMessage = diceService.rollDices(diceMessage, GAME_OWNER);

        // then
        List<Dice> dicesAfterRoll = gameMessage.getGame().getDices();
        assertAll(() -> dicesAfterRoll.forEach(
                dice -> assertFalse(dice.isImmutable())
        ));

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

    @Test
    public void should_check_number_and_has_possibility_to_next_roll() {

        // given
        List<Dice> dices = UtilsTests.setDicesAttributes(DiceModels.allFalseDices(1, 2, 1, 3, 5));
        dices.get(0).setChecked(true);
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, dices);
        prepareSimpleGame(OPEN);

        // when
        GameMessage gameMessage = diceService.checkDices(diceMessage, GAME_OWNER);

        // then
        assertTrue(gameMessage.getGame().getCurrentPlayer().getValidations().isRolling());
    }

    @Test
    public void should_return_message_with_error_when_player_who_checked_is_not_current_player() {

        // given
        String currentPlayerName = "PLAYER2";
        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, new ArrayList<>());
        Game game = GameBuilder.aGameBuilder()
                .withPlayers(List.of(new Player(0, GAME_OWNER), new Player(1, currentPlayerName)))
                .build();
        game.nextTurn();
        when(gameRepositoryMock.findById(any())).thenReturn(Optional.of(game));

        // when
        GameMessage gameMessage = diceService.checkDices(diceMessage, GAME_OWNER);

        // then
        assertEquals(ErrorContents.GAME_ERROR_BAD_OWNER.getContent(GAME_OWNER), gameMessage.getContent());
    }

    @Test
    public void should_count_temporary_points_after_roller_all_dices_second_time() {
        // given
        Game game = prepareSimpleGame(OPEN);

        List<Dice> firstRoll = DiceModels.allFalseDices(1, 1, 5, 5, 5);
        UtilsTests.setDicesAttributes(firstRoll);
        List<Dice> checkedFirstRoll = UtilsTests.setCheckedAllDices(firstRoll); // 70 pkt

        List<Dice> secondRoll = DiceModels.allFalseDices(1, 3, 3, 4, 6);
        UtilsTests.setDicesAttributes(secondRoll);
        List<Dice> checkedSecondRoll = UtilsTests.setCheckedAllDices(secondRoll); // 10 pkt

        DiceMessage firstDiceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, checkedFirstRoll);
        DiceMessage secondDiceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, checkedSecondRoll);

        // when
        diceService.checkDices(firstDiceMessage, GAME_OWNER);
        game.getPoints().managePointsBeforeNextRollByAllDices();

        GameMessage gameMessage = diceService.checkDices(secondDiceMessage, GAME_OWNER);

        // then
        assertEquals(80, gameMessage.getGame().getPoints().getPoints());
    }

    @Test
    public void should_count_correctly_after_checked_before_next_roll() {
        // given
        prepareSimpleGame(OPEN);

        List<Dice> firstRoll = DiceModels.allFalseDices(1, 5, 4, 3, 5);
        UtilsTests.setDicesAttributes(firstRoll);

        //FIRST REQUEST
        firstRoll.get(0).setChecked(true);
        firstRoll.get(1).setChecked(true);
        DiceMessage firstDiceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, firstRoll);
        GameMessage firstCheck = diceService.checkDices(firstDiceMessage, GAME_OWNER);

        assertEquals(15, firstCheck.getGame().getPoints().getPoints());

        //SECOND REQUEST
        firstRoll.get(1).setChecked(false);
        DiceMessage secondDiceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, firstRoll);
        GameMessage secondCheck = diceService.checkDices(secondDiceMessage, GAME_OWNER);

        assertEquals(10, secondCheck.getGame().getPoints().getPoints());

        //THIRD REQUEST
        firstRoll.get(1).setChecked(true);
        firstRoll.get(4).setChecked(true);
        DiceMessage thirdDiceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, firstRoll);
        GameMessage thirdCheck = diceService.checkDices(thirdDiceMessage, GAME_OWNER);

        assertEquals(20, thirdCheck.getGame().getPoints().getPoints());
    }

    @ParameterizedTest
    @MethodSource("rollsToSave")
    public void should_save_button_will_be_available_when_number_of_points_is_enough(
            List<Dice> fromRoll, int playerPoints, boolean isSaved
    ) {
        // given
        Game game = prepareSimpleGame(OPEN);
        game.getCurrentPlayer().setPoints(playerPoints);

        UtilsTests.setDicesAttributes(fromRoll);
        UtilsTests.setCheckedAllDices(fromRoll);

        DiceMessage diceMessage = new DiceMessage(GAME_ROLL.getType(), "", GAME_ID, fromRoll);

        // when
        GameMessage gameMessage = diceService.checkDices(diceMessage, GAME_OWNER);

        // then
        assertEquals(isSaved, gameMessage.getGame().getCurrentPlayer().getValidations().isSaved());

    }

    private Game prepareSimpleGame(GameStatus status) {
        Game game = GameBuilder.aGameBuilder()
                .withPlayers(List.of(new Player(0, GAME_OWNER)))
                .withGameStatus(status)
                .withPoints(new Points())
                .build();
        when(gameRepositoryMock.findById(any())).thenReturn(Optional.of(game));
        return game;
    }

    private static List<Dice> getDices(List<Dice> restDices) {
        List<Dice> dices = new ArrayList<>();
        dices.add(aDiceBuilder()
                .withValue(1)
                .withGoodNumber()
                .withChecked()
                .build());
        dices.add(aDiceBuilder()
                .withValue(5)
                .withGoodNumber()
                .withChecked()
                .build());
        dices.addAll(restDices);
        return dices;
    }

    private static List<Dice> getMultipleDicesByValue(){
        List<Dice> dices = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            dices.add(aDiceBuilder()
                    .withValue(3)
                    .withGoodNumber()
                    .withChecked()
                    .withImmutable()
                    .build());
        }
        return dices;
    }

    private static Stream<Arguments> rollsToSave(){
        return Stream.of(
                Arguments.of(DiceModels.allFalseDices(1, 1, 1, 3, 3), 0, true),
                Arguments.of(DiceModels.allFalseDices(1, 1, 5, 3, 3), 0, false),
                Arguments.of(DiceModels.allFalseDices(1, 1, 5, 3, 3), 100, true),
                Arguments.of(DiceModels.allFalseDices(1, 2, 5, 3, 3), 100, false)
        );
    }
}
