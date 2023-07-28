package com.example.DiceGameBE.controller;

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
    public ResponseEntity<String> addPlayer(
            @PathVariable("game-id") String gameId,
                                            @RequestParam String playerName){
        boolean isAdded = gamePlayersProvider.addPlayerToOpenGame(playerName, gameId);
        return isAdded
                ? ResponseEntity.ok("New Player has been added")
                : ResponseEntity.ok("Something went wrong, user was not added");
    }
}
