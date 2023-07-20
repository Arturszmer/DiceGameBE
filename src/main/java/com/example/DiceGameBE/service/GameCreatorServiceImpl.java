package com.example.DiceGameBE.service;

import com.example.DiceGameBE.dto.PlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.repository.GameDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameCreatorServiceImpl implements GameCreatorService{

    private final GameDao gameDaoImpl;
    private final ObjectMapper objectMapper;

    @Override
    public Game createGame(PlayerDto playerDto) {
        Game game = new Game(objectMapper.convertValue(playerDto, Player.class));
        return gameDaoImpl.saveGame(game);
    }

    @Override
    public Game findGame(String gameId) {
        String preKey = "game:";
        return gameDaoImpl.getGame(preKey + gameId).orElse(null);
    }
}
