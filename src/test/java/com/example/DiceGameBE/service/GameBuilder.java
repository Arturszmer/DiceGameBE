package com.example.DiceGameBE.service;

import com.example.DiceGameBE.model.Game;
import com.example.DiceGameBE.model.GameStatus;
import com.example.DiceGameBE.model.Player;
import com.example.DiceGameBE.model.Points;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class GameBuilder {

    private String gameId;
    private GameStatus gameStatus;
    private List<Player> players = new ArrayList<>();
    private Player adminPlayer;
    private Points points;

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

    public GameBuilder withPoints(Points points){
        this.points = points;
        return this;
    }

    public Game build() {
        Game game = new Game();
        game.setGameId(gameId);
        game.setGameStatus(gameStatus);
        game.setAdminPlayer(adminPlayer);
        game.setStartGameTime(LocalDateTime.now());
        game.getPlayers().addAll(players);
        game.setPoints(points);
        return game;
    }
}
