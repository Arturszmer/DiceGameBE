package com.example.DiceGameBE.service;


import com.example.DiceGameBE.dto.AdminPlayerDto;
import com.example.DiceGameBE.dto.message.GameMessage;
import com.example.DiceGameBE.dto.message.ConnectMessage;
import com.example.DiceGameBE.model.Game;

public interface GameCreatorService {

    Game createGame(AdminPlayerDto adminPlayerDto);
    GameMessage connectGame(ConnectMessage gameMessage);

    String deleteAllGames();
}