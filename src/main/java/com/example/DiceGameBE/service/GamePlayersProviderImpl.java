package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.DiceGameBE.assemblers.PlayerAssembler.toEntity;

@RequiredArgsConstructor
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository gameRepository;

    @Override
    public boolean addPlayerToOpenGame(CreatePlayerDto playerDto, String gameId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()){
            game.get().addPlayer(toEntity(playerDto));
            return true;
        } else {
            return false;
        }
    }
}
