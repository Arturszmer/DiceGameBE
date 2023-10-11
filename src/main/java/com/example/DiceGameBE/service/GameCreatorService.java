package com.example.DiceGameBE.service;


import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.JoinMessage;
import com.example.DiceGameBE.model.Game;

public interface GameCreatorService {

    Game createGame(AdminPlayerDto adminPlayerDto);
    GameMessage connectGame(JoinMessage gameMessage);

    String deleteAllGames();
}