package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GamePlayersProviderImpl implements GamePlayersProvider {

    private final GameRepository gameRepository;

    @Override
    public void addPlayerToOpenGame(CreatePlayerDto playerDto) {

    }
}
