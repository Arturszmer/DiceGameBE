package com.example.DiceGameBE.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
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
        players.add(adminPlayer);
        this.currentTurn = 0;
        this.startGameTime = LocalDateTime.now();
    }

    public void addPlayer(Player player){
        if(players.size() >= 4){
            throw new ArrayStoreException("The maximum number of players in one game is 4");
        }
        players.add(player);
    }
}
