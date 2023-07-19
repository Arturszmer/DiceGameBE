package com.example.DiceGameBE.service;


import com.example.DiceGameBE.dto.PlayerDto;
import com.example.DiceGameBE.model.Game;
import jakarta.servlet.http.HttpSession;

public interface GameCreatorService {

    Game createGame(PlayerDto playerDto);

}
