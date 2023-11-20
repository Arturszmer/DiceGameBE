package com.example.DiceGameBE.model;

import com.example.DiceGameBE.exceptions.GameException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AccessLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import static com.example.DiceGameBE.exceptions.GameErrorResult.*;

@Data
@RedisHash
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Game implements Serializable {

    @Id
    private String gameId;
    @EqualsAndHashCode.Include
    @Indexed
    private GameStatus gameStatus;
    private List<Player> players = new ArrayList<>();
    private Player adminPlayer;
    @Setter(AccessLevel.NONE)
    private Integer currentTurn = 0;
    private Points points;
    private LocalDateTime startGameTime;
    private List<Dice> dices = new ArrayList<>();
    @Setter(AccessLevel.NONE)
    @Indexed
    private String invitationToken;

    public Game(Player adminPlayer) {
        this.gameId = UUID.randomUUID().toString().replace("-", "");
        System.out.println(gameId);
        this.gameStatus = GameStatus.OPEN;
        this.adminPlayer = adminPlayer;
        getPlayers().add(adminPlayer);
        this.startGameTime = LocalDateTime.now();
        this.points = new Points();
    }

    public void addPlayer(String playerName){
        Player player = new Player(players.size(), playerName);
        addPlayerValidator(player);
        getPlayers().add(player);
    }

    private void addPlayerValidator(Player player) {
        if(gameStatus != GameStatus.OPEN){
            throw new GameException(ADD_PLAYER_TO_NOT_OPEN_GAME_EX);
        }
        if(players.size() >= 4){
            throw new GameException(GAME_PLAYERS_SIZE_EX);
        }
        if(player.getName().length() < 3){
            throw new GameException(PLAYER_NAME_MIN_LENGTH, player.getName().length());
        }
        boolean isPlayerUnique = players.stream()
                .anyMatch(p -> Objects.equals(p.getId(), player.getId())
                                || Objects.equals(p.getName(), player.getName()));
        if(isPlayerUnique){
            throw new GameException(UNIQUE_PLAYER_NAME_EX, player.getName());
        }
    }

    public void clearPoints(){
        this.points.clear();
    }

    public void nextTurn() {
        currentTurn++;
        if(currentTurn == players.size()){
            currentTurn = 0;
        }
        if(isPlayerInactive(currentTurn)){
            nextTurn();
        }
        getCurrentPlayer().getValidations().setRolling(true);
    }

    private boolean isPlayerInactive(Integer currentTurn) {
        return !players.get(currentTurn).isActive();
    }

    public Player getCurrentPlayer() {
        return players.get(currentTurn);
    }

    public Player getPlayerByName(String playerName) {
        return this.players.stream()
                .filter(player -> player.getName().equals(playerName))
                .findFirst()
                .orElseThrow(() -> new GameException(PLAYER_DOES_NOT_EXIST, playerName));
    }

    public boolean inactivePlayerByName(String toInactivePlayerName) {
        Player currentPlayer = getCurrentPlayer();
        if(currentPlayer != null && currentPlayer.getName().equals(toInactivePlayerName)){
            currentPlayer.setActive(false);
            currentPlayer.getValidations().setAllFalse();
            dices.clear();
            if(this.players.stream()
                    .filter(Player::isActive)
                    .toList()
                    .isEmpty()){
                return true;
            }
            nextTurn();
            return false;
        } else {
            getPlayerByName(toInactivePlayerName).setActive(false);
            return this.players.stream()
                    .filter(Player::isActive)
                    .toList()
                    .isEmpty();
        }
    }

    public String generateToken(){
        if(invitationToken == null){
            return invitationToken = UUID.randomUUID().toString();
        } else {
            return invitationToken;
        }
    }

    public void restart() {
        this.gameStatus = GameStatus.STARTED;
        this.players.forEach(Player::restart);
        this.currentTurn = 0;
        this.points = new Points();
        this.dices = new ArrayList<>();
    }
}
