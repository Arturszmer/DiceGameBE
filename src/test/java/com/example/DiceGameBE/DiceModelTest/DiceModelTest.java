package com.example.DiceGameBE.DiceModelTest;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import com.example.DiceGameBE.service.GamePlayersProvider;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static com.example.DiceGameBE.DiceModelTest.DiceModel.builDiceModelGame;
import static com.example.DiceGameBE.model.GameStatus.OPEN;
import static com.example.DiceGameBE.service.models.GAME_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceModelTest {

    private GamePlayersProvider playersProvider;

    private final GameRepository gameRepository = mock(GameRepository.class);

    @Test
    public void ShouldAddAdminPlayerToDickGameWithStatusOpen(){

//given
        Game game = builDiceModelGame();
        game.setGameStatus(OPEN);

//when
        when(gameRepository.findById(GAME_ID)).thenReturn(Optional.of(game));


//then
        Game savedGame = gameRepository.findById(GAME_ID).orElseThrow();
        assertThrows(RuntimeException.class, () -> playersProvider.addPlayerToOpenGame("ADMIN", GAME_ID));
        assertEquals(1,savedGame.getPlayers().size());
    }
}

