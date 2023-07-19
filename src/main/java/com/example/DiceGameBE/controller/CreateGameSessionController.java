package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.dto.PlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.service.GameCreatorService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateGameSessionController {

    private final GameCreatorService gameCreatorService;
    @PostMapping("/start-game")
    public String startGame(@RequestBody PlayerDto playerName){
        Game game = gameCreatorService.createGame(playerName);
        return "Start game! The game id is: " + game.getGameId();
    }


}
