package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.DiceGameBE.service.models.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
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


}
