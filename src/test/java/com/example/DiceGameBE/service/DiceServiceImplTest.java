package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.DiceMessage;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.model.Dice;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.DiceServiceImpl;
import com.example.DiceGameBE.utils.ErrorContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.DiceGameBE.model.GameStatus.FINISHED;
import static com.example.DiceGameBE.model.GameStatus.OPEN;
import static com.example.DiceGameBE.service.DiceBuilder.*;
import static com.example.DiceGameBE.utils.MessageTypes.*;
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
        List<Dice> dices = getDices(getMultipleDicesByValue(3, true));
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

    private void prepareSimpleGame(GameStatus status) {
        Game game = GameBuilder.aGameBuilder()
                .withPlayers(List.of(new Player(0, GAME_OWNER)))
                .withGameStatus(status)
                .build();
        when(gameRepositoryMock.findById(any())).thenReturn(Optional.of(game));
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

    private static List<Dice> getMultipleDicesByValue(int value, boolean isImmutable){
        List<Dice> dices = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            if(isImmutable) {
                dices.add(aDiceBuilder()
                        .withValue(value)
                        .withGoodNumber()
                        .withChecked()
                        .withImmutable()
                        .build());
            } else {
                dices.add(aDiceBuilder()
                        .withValue(value)
                        .withGoodNumber()
                        .withChecked()
                        .build());
            }
        }
        return dices;
    }
}
