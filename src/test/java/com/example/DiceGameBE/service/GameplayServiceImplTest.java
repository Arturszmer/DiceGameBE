package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.Message;
import com.example.DiceGameBE.dto.message.SimpMessage;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.GameplayServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static com.example.DiceGameBE.service.GameModels.GAME_ID;
import static com.example.DiceGameBE.service.GameModels.buildSimpleGame;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameplayServiceImplTest {

    private GameplayService gameplayService;
    private final GameRepository gameRepository = mock(GameRepository.class);

    @BeforeEach
    void setup(){
        gameplayService = new GameplayServiceImpl(gameRepository);
    }

    @Test
    public void should_be_able_to_change_player(){
        // given
        String player = "Player2";
        Game game = buildSimpleGame(player);
        Message message = SimpMessage.builder()
                .gameId(GAME_ID)
                .build();
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.ofNullable(game));

        // when
        assertEquals(0, Objects.requireNonNull(game).getCurrentTurn());
        GameMessage gameMessage = gameplayService.nextPlayer(message);

        // then
        assertEquals(1, gameMessage.getGame().getCurrentTurn());
        assertEquals(player, gameMessage.getGame().getCurrentPlayer().getName());
        assertTrue(gameMessage.getCurrentPlayer().getValidations().isRolling());
    }

    @Test
    public void should_change_active_player_to_first_player_on_turn_change() {
        // given
        String player = "Player2";
        Game game = buildSimpleGame(player);
        game.nextTurn();

        Message message = SimpMessage.builder()
                .gameId(GAME_ID)
                .build();
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));

        // when
        GameMessage gameMessage = gameplayService.nextPlayer(message);

        // then
        assertEquals(0, gameMessage.getGame().getCurrentTurn());
        assertNotEquals(player, gameMessage.getGame().getCurrentPlayer().getName());
        assertFalse(gameMessage.getGame().getPlayerByName(player).getValidations().isRolling());

    }


}