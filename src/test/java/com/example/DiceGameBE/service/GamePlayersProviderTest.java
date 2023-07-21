package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.example.DiceGameBE.service.models.buildSimpleGame;
import static com.example.DiceGameBE.service.models.createSimplePlayerDto;
import static org.mockito.Mockito.mock;

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
        playersProvider.addPlayerToOpenGame(createSimplePlayerDto(1, "user"), game.getGameId());

        //then

    }


}
