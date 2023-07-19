package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.PlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameCreatorServiceImpl implements GameCreatorService{

    private final GameRepository gameRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Game createGame(PlayerDto playerDto) {
        Game game = new Game(objectMapper.convertValue(playerDto, Player.class));
        String savedGame = gameRepository.saveGame(game);
        return objectMapper.convertValue(savedGame, Game.class);
    }
}
