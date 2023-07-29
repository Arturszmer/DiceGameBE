package com.example.DiceGameBE.service;


import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.model.Game;

public interface GameCreatorService {

    Game createGame(AdminPlayerDto adminPlayerDto);

    String deleteAllGames();
}