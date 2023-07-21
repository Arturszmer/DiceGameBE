package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameCreatorServiceImplTest {

    private GameCreatorService gameCreatorService;
    GameRepository gameRepository = mock(GameRepository.class);
    private static final String GAME_ID = UUID.randomUUID()
            .toString().replace("-", "");

    @BeforeEach
    void setup(){
        gameCreatorService = new GameCreatorServiceImpl(gameRepository);
    }

    @Test
    public void should_create_game() {
        // given
        CreatePlayerDto adminPlayer = createAdminPlayer();
        Player franek = new Player(0, "Franek");

        Game game = GameBuilder.aGameBuilder()
                .withGameId(GAME_ID)
                .withGameStatus(GameStatus.OPEN)
                .withAdminPlayer(franek)
                .withPlayers(List.of(franek))
                .build();
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

    private CreatePlayerDto createAdminPlayer() {
        return new CreatePlayerDto(0, "Franek");
    }

}