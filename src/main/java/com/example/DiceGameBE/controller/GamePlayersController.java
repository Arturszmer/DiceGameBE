package com.example.DiceGameBE.controller;

import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.service.GamePlayersProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GamePlayersController {

    private final GamePlayersProvider gamePlayersProvider;

    @PostMapping("/{game-id}/add-player")
    public ResponseEntity<Player> addPlayer(
            @PathVariable("game-id") String gameId,
                                            @RequestParam String playerName){
        Player addedPlayer = gamePlayersProvider.addPlayerToOpenGame(playerName, gameId);
        return ResponseEntity.ok(addedPlayer);
    }
}
