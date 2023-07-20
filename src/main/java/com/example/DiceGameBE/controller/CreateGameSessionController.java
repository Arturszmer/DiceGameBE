package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.dto.CreatePlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.service.GameCreatorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreateGameSessionController {

    private final GameCreatorService gameCreatorService;
    public CreateGameSessionController(GameCreatorService gameCreatorService) {
        this.gameCreatorService = gameCreatorService;
    }

    @PostMapping("/start-game")
    public ResponseEntity<Game> startGame(@RequestBody CreatePlayerDto playerName){
        Game game = gameCreatorService.createGame(playerName);
        return ResponseEntity.ok(game);
    }

    @GetMapping("/find-game")
    public ResponseEntity<Game> gameDetails(@RequestParam("gameId") String gameId){
        return ResponseEntity.ok(gameCreatorService.findGame(gameId));
    }


}
