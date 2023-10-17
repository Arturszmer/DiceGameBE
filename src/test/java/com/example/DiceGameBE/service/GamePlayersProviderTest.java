package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.SimpMessage;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.GamePlayersProviderImpl;
import com.example.DiceGameBE.utils.GameplayContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.DiceGameBE.model.GameStatus.*;
import static com.example.DiceGameBE.service.GameModels.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GamePlayersProviderTest {

    private GamePlayersProvider playersProvider;
    private final GameRepository repository = mock(GameRepository.class);

    @BeforeEach
    void setup(){
        playersProvider = new GamePlayersProviderImpl(repository);
    }

    @Test
    void should_add_players_to_open_game() {
        //given
        Game game = buildSimpleGame();

        //when
        when(repository.findById(GAME_ID)).thenReturn(Optional.ofNullable(game));

        playersProvider.joinToOpenGame(new NewPlayerDto("user"), GAME_ID);

        //then
        assert game != null;
        assertEquals("user", game.getPlayers().get(1).getName());
        assertEquals(1, game.getPlayers().get(1).getId());
        assertEquals(0, game.getPlayers().get(1).getPoints());
    }

    @Test
    public void shouldNotAddPlayerToGameWithDifferentStatusThanOpen(){
        // given
        Game game = buildSimpleGame();
        game.setGameStatus(STARTED);

        // when
        when(repository.findById(GAME_ID)).thenReturn(Optional.of(game));

        // then
        assertThrows(RuntimeException.class, () -> playersProvider.joinToOpenGame(new NewPlayerDto("user"), GAME_ID));

    }

    @ParameterizedTest
    @MethodSource("checkExceptionsForAddPlayerMethod")
    public void should_check_all_validators_in_add_player_method(Game game, NewPlayerDto player, Class<Exception> exception, GameStatus gameStatus) {
        // given
        game.setGameStatus(gameStatus);

        // when
        when(repository.findById(GAME_ID)).thenReturn(Optional.of(game));

        // then
        assertThrows(exception,
                () -> playersProvider.joinToOpenGame(player, GAME_ID));
    }

    @Test
    public void should_leave_the_game() {
        // given
        String playerToLeave = "Player1";
        SimpMessage message = SimpMessage.builder()
                .gameId(GAME_ID).build();
        Game game = buildSimpleGame(playerToLeave, "Player2");
        when(repository.findById(GAME_ID)).thenReturn(Optional.of(game));

        // when
        assertEquals(playerToLeave, game.getPlayerByName(playerToLeave).getName());
        GameMessage gameMessage = playersProvider.leaveGame(message, playerToLeave);

        // then
        assertEquals(2, gameMessage.getGame().getPlayers().size());
        assertThrows(GameException.class, () -> gameMessage.getGame().getPlayerByName(playerToLeave));
        assertEquals(GameplayContents.DISCONNECT.getContent(playerToLeave), gameMessage.getContent());

    }

    @Test
    public void should_change_player_turn_if_current_player_left_game() {
        // given
        String playerToLeave = "Player1";
        String nextPlayer = "Player2";
        SimpMessage message = SimpMessage.builder()
                .gameId(GAME_ID).build();
        Game game = buildSimpleGame(playerToLeave, nextPlayer);
        game.nextTurn();
        when(repository.findById(GAME_ID)).thenReturn(Optional.of(game));

        // when
        assertEquals(playerToLeave, game.getCurrentPlayer().getName());
        GameMessage gameMessage = playersProvider.leaveGame(message, playerToLeave);

        // then
        //TODO: do poprawienia logika wychodzenia gra z gry
        assertEquals(nextPlayer, gameMessage.getCurrentPlayer().getName());
        assertTrue(gameMessage.getCurrentPlayer().getValidations().isRolling());

    }

    private static Stream<Arguments> checkExceptionsForAddPlayerMethod(){
        return Stream.of(
                Arguments.of(buildSimpleGame("player2", "player3"), new NewPlayerDto("ar"), GameException.class, OPEN),
                Arguments.of(buildSimpleGame("player2", "player3", "player4"),  new NewPlayerDto("user4"), GameException.class, OPEN),
                Arguments.of(buildSimpleGame(),  new NewPlayerDto("user1"), GameException.class, FINISHED),
                Arguments.of(buildSimpleGame("player2"),  new NewPlayerDto("player2"), GameException.class, OPEN)
                );
    }
}