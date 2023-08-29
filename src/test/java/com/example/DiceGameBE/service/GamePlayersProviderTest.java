package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.exceptions.GameException;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.GamePlayersProviderImpl;
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

        playersProvider.addPlayerToOpenGame(new NewPlayerDto("user"), GAME_ID);

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
        assertThrows(RuntimeException.class, () -> playersProvider.addPlayerToOpenGame(new NewPlayerDto("user"), GAME_ID));

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
                () -> playersProvider.addPlayerToOpenGame(player, GAME_ID));
    }

    private static Stream<Arguments> checkExceptionsForAddPlayerMethod(){
        return Stream.of(
                Arguments.of(buildSimpleGame(2), new NewPlayerDto("ar"), GameException.class, OPEN),
                Arguments.of(buildSimpleGame(3),  new NewPlayerDto("user4"), GameException.class, OPEN),
                Arguments.of(buildSimpleGame(),  new NewPlayerDto("user1"), GameException.class, FINISHED),
                Arguments.of(buildSimpleGame(1),  new NewPlayerDto("user1"), GameException.class, OPEN)
                );
    }
}