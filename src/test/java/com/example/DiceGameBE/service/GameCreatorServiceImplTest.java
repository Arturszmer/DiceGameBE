package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.impl.GameCreatorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.example.DiceGameBE.service.models.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameCreatorServiceImplTest {

    private GameCreatorService gameCreatorService;
    GameRepository gameRepository = mock(GameRepository.class);

    @BeforeEach
    void setup(){
        gameCreatorService = new GameCreatorServiceImpl(gameRepository);
    }

    @Test
    public void should_create_game() {
        // given
        AdminPlayerDto adminPlayer = createAdminPlayerDto();
        Game game = buildSimpleGame();

        // when
        when(gameRepository.save(any())).thenReturn(game);
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.ofNullable(game));
        gameCreatorService.createGame(adminPlayer);

        // then
        Game savedGame = gameRepository.findById(GAME_ID).orElseThrow();
        assertEquals(GAME_ID, savedGame.getGameId());
        assertEquals(GameStatus.OPEN, savedGame.getGameStatus());
        assertEquals(1, savedGame.getPlayers().size());
    }

}