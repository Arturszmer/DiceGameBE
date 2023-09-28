package com.example.DiceGameBE.controller.rest;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.service.GamesSearchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class GamesSearchingController {

    private final GamesSearchingService gamesSearchingService;

    @GetMapping("/find-game")
    public ResponseEntity<Game> findGameById(@RequestParam("gameId") String gameId){
        return ResponseEntity.ok(gamesSearchingService.findGameByGameId(gameId));
    }

    @GetMapping("/find-open-games")
    public ResponseEntity<List<Game>> findOpenGames(){
        return ResponseEntity.ok(gamesSearchingService.findOpenGames(GameStatus.OPEN));
    }

    @GetMapping("/find-open-games-page")
    public ResponseEntity<Page<Game>> findOpenGames(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "5") int size){
        return ResponseEntity.ok(gamesSearchingService.findGamesByPage(page, size));
    }

}
