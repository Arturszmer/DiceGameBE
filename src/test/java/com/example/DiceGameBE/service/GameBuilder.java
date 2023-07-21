package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class GameBuilder {

    private String gameId;
    private GameStatus gameStatus;
    private List<Player> players = new ArrayList<>();
    private Player adminPlayer;
    private Integer currentTurn = 0;
    private LocalDateTime startGameTime = LocalDateTime.now();

    public static GameBuilder aGameBuilder() {
        return new GameBuilder();
    }

    public GameBuilder withGameId(String gameId) {
        this.gameId = gameId;
        return this;
    }

    public GameBuilder withGameStatus(GameStatus status) {
        this.gameStatus = status;
        return this;
    }

    public GameBuilder withPlayers(List<Player> players) {
        this.players = players;
        return this;
    }

    public GameBuilder withAdminPlayer(Player player) {
        this.adminPlayer = player;
        return this;
    }

    public Game build() {
        return new Game(
                gameId,
                gameStatus,
                players,
                adminPlayer,
                currentTurn,
                startGameTime
        );
    }
}
