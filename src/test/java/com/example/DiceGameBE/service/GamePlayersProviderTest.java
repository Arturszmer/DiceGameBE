package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static com.example.DiceGameBE.service.models.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GamePlayersProviderTest {

    private GamePlayersProvider playersProvider;
    private GameRepository repository = mock(GameRepository.class);

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

        boolean isAdded = playersProvider.addPlayerToOpenGame(createSimplePlayerDto(1, "user"), GAME_ID);

        //then
        assertTrue(isAdded);
    }

    @ParameterizedTest
    @MethodSource("checkExceptionsForAddPlayerMethod")
    public void should_check_all_validators_in_add_player_method(Game game, CreatePlayerDto player, Class<Exception> exception, GameStatus gameStatus) {
        // given
        game.setGameStatus(gameStatus);

        // when
        when(repository.findById(GAME_ID)).thenReturn(Optional.ofNullable(game));

        // then
        assertThrows(exception,
                () -> playersProvider.addPlayerToOpenGame(player, GAME_ID));
    }

    private static Stream<Arguments> checkExceptionsForAddPlayerMethod(){
        return Stream.of(
                Arguments.of(buildSimpleGame(3), createSimplePlayerDto(4, "user4"), ArrayStoreException.class, GameStatus.OPEN),
                Arguments.of(buildSimpleGame(), createSimplePlayerDto(1, "user1"), IllegalStateException.class, GameStatus.FINISHED),
                Arguments.of(buildSimpleGame(1), createSimplePlayerDto(1, "userUnique"), RuntimeException.class, GameStatus.OPEN),
                Arguments.of(buildSimpleGame(1), createSimplePlayerDto(2, "user1"), RuntimeException.class, GameStatus.OPEN)
                );
    }
}