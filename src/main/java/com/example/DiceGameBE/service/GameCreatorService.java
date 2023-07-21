package com.example.DiceGameBE.service;


import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;

import java.util.List;

public interface GameCreatorService {

    Game createGame(CreatePlayerDto createPlayerDto);

    String deleteAllGames();
}