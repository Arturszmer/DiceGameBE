package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.dto.AdminPlayerDto;
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

    @PostMapping("/create-game")
    public ResponseEntity<Game> createGame(@RequestBody AdminPlayerDto playerName){
        Game game = gameCreatorService.createGame(playerName);
        return ResponseEntity.ok(game);
    }

    @DeleteMapping("/delete-all-games")
    public ResponseEntity<String> deleteAllGames(){
        return ResponseEntity.ok(gameCreatorService.deleteAllGames());
    }

}
