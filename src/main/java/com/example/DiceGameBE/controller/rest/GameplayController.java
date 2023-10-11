package com.example.DiceGameBE.controller.rest;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.service.GameplayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class GameplayController {

    private final GameplayService gameplayService;
    @PostMapping("/nextPlayer")
    public Game nextPlayer(@RequestBody String gameId){
        return gameplayService.nextPlayer(gameId);
    }
}
