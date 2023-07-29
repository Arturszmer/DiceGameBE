package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository gameRepository;

    @Override
    public Player addPlayerToOpenGame(String playerName, String gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent() && game.get().getGameStatus() == GameStatus.OPEN){
            game.get().addPlayer(playerName);
            log.info("New Player has been added, his name is: {}", playerName);
            gameRepository.save(game.get());
            return game.get().getPlayers().stream()
                    .filter(p -> p.getName().equals(playerName))
                    .findFirst()
                    .orElseThrow();
        } else {
            throw new RuntimeException("Something went wrong, the game is closed or not exist");
        }
    }
}
