package com.example.DiceGameBE.controller.rest;

import com.example.DiceGameBE.dto.NewPlayerDto;
import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.service.GamePlayersProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class GamePlayersController {

    private final GamePlayersProvider gamePlayersProvider;

    @PostMapping("/{game-id}/add-player")
    public ResponseEntity<Game> addPlayer(@PathVariable("game-id") String gameId,
                                          @RequestBody NewPlayerDto playerName){
        Game game = gamePlayersProvider.joinToOpenGame(playerName, gameId);
        return ResponseEntity.ok(game);
    }
}
