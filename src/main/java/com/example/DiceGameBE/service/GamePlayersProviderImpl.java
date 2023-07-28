package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
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
    public boolean addPlayerToOpenGame(String playerName, String gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()){
            game.get().addPlayer(playerName);
            log.info("New Player has been added, his name is: {}", playerName);
            gameRepository.save(game.get());
            return true;
        } else {
            return false;
        }
    }
}
