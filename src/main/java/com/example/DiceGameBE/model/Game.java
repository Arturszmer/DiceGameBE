package com.example.DiceGameBE.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Game implements Serializable {

//    @EqualsAndHashCode.Include
    @Id
    private String gameId;
    @EqualsAndHashCode.Include
    @Indexed
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
        getPlayers().add(adminPlayer);
        this.currentTurn = 0;
        this.startGameTime = LocalDateTime.now();
    }

    public void addPlayer(String playerName){
        Player player = new Player(players.size(), playerName);
        addPlayerValidator(player);
        getPlayers().add(player);
    }

    private void addPlayerValidator(Player player) {
        if(players.size() >= 4){
            throw new ArrayStoreException("The maximum number of players in one game is 4");
        }
        if(gameStatus == GameStatus.FINISHED){
            throw new IllegalStateException("you cannot add players because game is finished");
        }
        boolean isPlayerUnique = players.stream()
                .anyMatch(p -> Objects.equals(p.getId(), player.getId())
                                || Objects.equals(p.getName(), player.getName()));
        if(isPlayerUnique){
            throw new RuntimeException("The new player name is not unique in this game");
        }
    }
}
