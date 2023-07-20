package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.dto.PlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.service.GameCreatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CreateGameSessionController {

    private final GameCreatorService gameCreatorService;
    @PostMapping("/start-game")
    public ResponseEntity<Game> startGame(@RequestBody PlayerDto playerName){
        Game game = gameCreatorService.createGame(playerName);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/find-game")
    public Game gameDetails(@RequestParam("gameId") String gameId){
        return gameCreatorService.findGame(gameId);
    }


}
