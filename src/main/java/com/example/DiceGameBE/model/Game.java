package com.example.DiceGameBE.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class Game implements Serializable {

    private String gameId;
    private GameStatus gameStatus;
    private List<Player> players = new ArrayList<>();
    private Player adminPlayer;
    private Integer currentTurn;
    private LocalDateTime startGameTime;

    public Game(Player adminPlayer) {
        this.gameId = UUID.randomUUID().toString().replace("-", "");
        System.out.println(gameId);
        this.gameStatus = GameStatus.OPEN;
        this.adminPlayer = adminPlayer;
        players.add(adminPlayer);
        this.currentTurn = 0;
        this.startGameTime = LocalDateTime.now();
    }
}
