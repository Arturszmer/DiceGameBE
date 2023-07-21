package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameDao;
import com.example.DiceGameBE.repository.GameDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameCreatorServiceImplTest {

    private GameCreatorService gameCreatorService;
    GameDao gameDao = mock(GameDaoImpl.class);
    private static final String GAME_ID = UUID.randomUUID()
            .toString().replace("-", "");

    @BeforeEach
    void setup(){
        gameCreatorService = new GameCreatorServiceImpl(gameDao);
    }

    @Test
    public void should_create_game() throws Exception {
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
        when(gameDao.saveGame(any())).thenReturn(game);
        gameCreatorService.createGame(adminPlayer);

        System.out.println(game.getGameId());
        // then

    }

    private CreatePlayerDto createAdminPlayer() {
        return new CreatePlayerDto(0, "Franek");
    }

}